/*
 * Entity.java
 * Copyright 2017 Sebastian Fernandez Ledesma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sfernandezledesma.entities;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sfernandezledesma.physics.AABB;
import com.sfernandezledesma.physics.CollisionQuadtree;

public abstract class Entity {
    private static int next_id = 1;
    protected int myID;
    protected AABB box;
    protected Sprite sprite;
    protected Vector2 offsetsSprite;
    protected CollisionQuadtree quadtree = null;
    protected boolean toBeDestroyed = false;

    public Entity(AABB box, Sprite sprite, Vector2 offsetsSprite) {
        assignId();
        this.box = box;
        this.sprite = sprite; // For now lets assume all entities have a sprite
        if (offsetsSprite == null)
            this.offsetsSprite = new Vector2();
        else
            this.offsetsSprite = offsetsSprite;
    }

    protected void assignId() {
        myID = next_id;
        next_id = next_id + 1;
    }

    public AABB getBox() {
        return box;
    }

    public boolean setX(double x) {
        quadtree.remove(this);
        box.setX(x);
        return quadtree.add(this);
    }

    public boolean setY(double y) {
        quadtree.remove(this);
        box.setY(y);
        return quadtree.add(this);
    }

    public double getX() {
        return box.getX();
    }

    public double getY() {
        return box.getY();
    }

    public boolean translateX(double dx) {
        quadtree.remove(this);
        box.setX(box.getX() + dx);
        return quadtree.add(this);
    }

    public boolean translateY(double dy) {
        quadtree.remove(this);
        box.setY(box.getY() + dy);
        return quadtree.add(this);
    }

    private float getOffsetSpriteX() {
        return offsetsSprite.x;
    }

    private float getOffsetSpriteY() {
        return offsetsSprite.y;
    }

    public void handleInput() {
    }

    public void update(float delta) {
    }

    public abstract boolean onCollision(Entity entity, World world, float delta);
    public boolean onCollisionWith(DynamicEntity otherDynamicEntity, World world, float delta) { return true; }
    public boolean onCollisionWith(StaticEntity otherStaticEntity, World world, float delta) { return true; }
    public boolean onCollisionWith(Hero hero, World world, float delta) { return true; }

    public void render(SpriteBatch batch) {
        sprite.setPosition((float)getX() - getOffsetSpriteX(), (float)getY() - getOffsetSpriteY());
        sprite.draw(batch);
    }

    public CollisionQuadtree getQuadtree() {
        return quadtree;
    }

    public void setQuadtree(CollisionQuadtree quadtree) {
        this.quadtree = quadtree;
    }

    public boolean isToBeDestroyed() {
        return toBeDestroyed;
    }

    public void setToBeDestroyed(boolean toBeDestroyed) {
        this.toBeDestroyed = toBeDestroyed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return myID == entity.myID;

    }

    @Override
    public int hashCode() {
        return myID;
    }
}