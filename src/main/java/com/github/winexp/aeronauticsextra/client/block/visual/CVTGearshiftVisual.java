package com.github.winexp.aeronauticsextra.client.block.visual;

import com.github.winexp.aeronauticsextra.client.AeroExtraPartialModels;
import com.github.winexp.aeronauticsextra.content.blocks.kinetics.CVTGearshiftBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class CVTGearshiftVisual extends SingleAxisRotatingVisual<CVTGearshiftBlockEntity> {
    private final RotatingInstance cogInstance;

    public CVTGearshiftVisual(VisualizationContext context, CVTGearshiftBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFT));
        this.cogInstance = this.instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AeroExtraPartialModels.CVT_GEARSHIFT_COG)).createInstance()
                .rotateToFace(Direction.UP, this.rotationAxis())
                .setup(blockEntity.getExtraKinetics())
                .setPosition(this.getVisualPosition());;
    }

    @Override
    public void update(float pt) {
        super.update(pt);
        this.cogInstance.setup(this.blockEntity.getExtraKinetics()).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        this.relight(this.cogInstance);
    }

    @Override
    protected void _delete() {
        super._delete();
        this.cogInstance.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(this.cogInstance);
    }
}
