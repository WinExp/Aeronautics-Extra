package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3d;
import org.joml.Vector3d;

import java.util.List;

public class TrilaterationResolver {
    public static LocateResult locate(List<SampleData> sampleDataList) {
        if (sampleDataList.size() == 4) {
            Vec3 pos = trilateration4Points(
                    sampleDataList.stream().map(SampleData::satellitePosition).toList(),
                    sampleDataList.stream().map(SampleData::distance).toList()
            );
            if (pos == null) return LocateResult.of(LocateResult.FailureReason.SINGULAR, null);
            return LocateResult.ofSuccess(pos);
        } else if (sampleDataList.size() > 4) {
            Vec3 pos = simpleLeastSquares(
                    sampleDataList.stream().map(SampleData::satellitePosition).toList(),
                    sampleDataList.stream().map(SampleData::distance).toList(),
                    sampleDataList.stream().map(SampleData::signalStrength).toList()
            );
            if (pos == null) return LocateResult.of(LocateResult.FailureReason.SINGULAR, null);
            return LocateResult.ofSuccess(pos);
        }
        return LocateResult.of(LocateResult.FailureReason.SATELLITE_NOT_ENOUGH, null);
    }

    public static Vec3 simpleLeastSquares(List<Vec3> positions, List<Double> distances, List<Float> signalStrengths) {
        int n = positions.size();
        if (n < 4) return null;

        // 根据信号强度计算权重（误差反比 => 权重 ∝ 信号强度的平方）
        double[] w = new double[n];
        for (int i = 0; i < n; i++) {
            double s = signalStrengths.get(i);
            // 防止零或负值导致除零/异常，可加保护
            w[i] = (s > 1e-6) ? s * s : 1e-12;
        }

        Vector3d p = new Vector3d();
        for (Vec3 pos : positions) p.add(pos.x, pos.y, pos.z);
        p.div(n);
        Vec3 triPos = trilateration4Points(positions, distances);
        if (triPos != null) p = new Vector3d(triPos.x, triPos.y, triPos.z);

        // 高斯-牛顿迭代，固定10次，不做过细的收敛判断（精度要求不高）
        for (int iter = 0; iter < 10; iter++) {
            Matrix3d JtJ = new Matrix3d();   // 3x3 零矩阵
            Vector3d Jtr = new Vector3d();   // 3维零向量

            for (int i = 0; i < n; i++) {
                Vec3 sat = positions.get(i);
                double dx = p.x - sat.x;
                double dy = p.y - sat.y;
                double dz = p.z - sat.z;
                double pred = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (pred < 1e-12) continue;
                double res = pred - distances.get(i);   // 残差
                double jx = dx / pred;
                double jy = dy / pred;
                double jz = dz / pred;

                double wi = w[i];
                // 累加 J^T J
                JtJ.m00 += wi * jx * jx;
                JtJ.m01 += wi * jx * jy;
                JtJ.m02 += wi * jx * jz;
                JtJ.m11 += wi * jy * jy;
                JtJ.m12 += wi * jy * jz;
                JtJ.m22 += wi * jz * jz;
                // 累加 J^T r
                Jtr.x += wi * jx * res;
                Jtr.y += wi * jy * res;
                Jtr.z += wi * jz * res;
            }
            // 对称填充
            JtJ.m10 = JtJ.m01; JtJ.m20 = JtJ.m02; JtJ.m21 = JtJ.m12;

            // 解方程 (J^T J) * delta = -J^T r
            double det = JtJ.determinant();
            if (Math.abs(det) < 1e-9) break; // 奇异则停止迭代
            Matrix3d inv = new Matrix3d();
            JtJ.invert(inv);
            Vector3d delta = inv.transform(new Vector3d(-Jtr.x, -Jtr.y, -Jtr.z));

            // 更新位置
            p.add(delta);
            // 如果增量已经很小，提前结束
            if (delta.length() < 1e-6) break;
        }

        return new Vec3(p.x, p.y, p.z);
    }

    public static Vec3 trilateration4Points(List<Vec3> positions, List<Double> distances) {
        if (positions.size() < 4) return null;
        Vec3 pos1 = positions.getFirst();
        double x0 = pos1.x, y0 = pos1.y, z0 = pos1.z;
        double d0 = distances.getFirst();

        // 构建系数矩阵 A 和 常数项 b
        Matrix3d A = new Matrix3d();
        Vector3d b = new Vector3d();

        for (int i = 1; i <= 3; i++) {
            Vec3 posI = positions.get(i);
            double xi = posI.x, yi = posI.y, zi = posI.z;
            double di = distances.get(i);

            // 设置 A 的第 (i-1) 行：[xi - x0, yi - y0, zi - z0]
            A.setRow(i - 1, xi - x0, yi - y0, zi - z0);

            // 计算并设置 b 的第 (i-1) 个元素
            double rhs = (d0 * d0 - di * di + (xi * xi + yi * yi + zi * zi) - (x0 * x0 + y0 * y0 + z0 * z0)) / 2.0;
            b.setComponent(i-1, rhs);
        }

        // 检查行列式，判断矩阵是否可逆
        double det = A.determinant();
        if (Math.abs(det) < 1e-12) {
            return null;
        }

        // 求逆并计算解
        Matrix3d invA = new Matrix3d();
        A.invert(invA);
        Vector3d result = invA.transform(b);
        return new Vec3(result.x, result.y, result.z);
    }

    public record LocateResult(FailureReason failureReason, Vec3 position) {
        public static LocateResult ofSuccess(Vec3 position) {
            return new LocateResult(FailureReason.SUCCESS, position);
        }

        public static LocateResult of(FailureReason failureReason, Vec3 position) {
            return new LocateResult(failureReason, position);
        }

        public boolean isEmpty() {
            return this.position == null;
        }

        public enum FailureReason {
            SUCCESS, SINGULAR, SATELLITE_NOT_ENOUGH
        }
    }
}
