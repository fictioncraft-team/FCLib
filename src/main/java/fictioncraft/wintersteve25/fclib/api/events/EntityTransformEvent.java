package fictioncraft.wintersteve25.fclib.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class EntityTransformEvent extends Event {

    private final Entity entityOld;
    private final EntityType<?> entityNew;

    public EntityTransformEvent(Entity entityOld, EntityType<?> entityNew) {
        this.entityOld = entityOld;
        this.entityNew = entityNew;
    }

    public Entity getEntityOld() {
        return entityOld;
    }

    public EntityType<?> getEntityNew() {
        return entityNew;
    }

    @Cancelable
    public static class Pre extends EntityTransformEvent {
        public Pre(Entity entityOld, EntityType<?> entityNew) {
            super(entityOld, entityNew);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends EntityTransformEvent {
        public Post(Entity entityOld, EntityType<?> entityNew) {
            super(entityOld, entityNew);
        }

        @Override
        public boolean isCancelable() {
            return false;
        }
    }
}
