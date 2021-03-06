/*
 * World.java
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

package com.sfernandezledesma.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfernandezledesma.entities.DynamicEntity;
import com.sfernandezledesma.entities.Entity;
import com.sfernandezledesma.entities.StaticEntity;
import com.sfernandezledesma.physics.AABB;
import com.sfernandezledesma.physics.CollisionQuadtree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class World {
    private double width;
    private double height;
    private HashSet<DynamicEntity> dynamicEntities = new HashSet<DynamicEntity>();
    private HashSet<StaticEntity> staticEntities = new HashSet<StaticEntity>();
    private CollisionQuadtree quadtree;
    private AssetManager assetManager;

    public World(double width, double height, AssetManager assetManager) {
        this.width = width;
        this.height = height;
        quadtree = new CollisionQuadtree(0, new AABB(0, 0, width, height), null);
        this.setAssetManager(assetManager);
    }

    public void addDynamicEntity(DynamicEntity entity) {
        getDynamicEntities().add(entity);
        quadtree.add(entity);
    }

    public void addStaticEntity(StaticEntity entity) {
        getStaticEntities().add(entity);
        quadtree.add(entity);
    }

    public void update(float delta) {
        for (DynamicEntity e : dynamicEntities) {
            e.update(delta);
        }

        Iterator<DynamicEntity> it = dynamicEntities.iterator();
        while (it.hasNext()) {
            DynamicEntity e = it.next();
            if (e.isToBeDestroyed()) {
                it.remove();
            } else {
                e.setUpdating(false);
            }
        }
        // If static objects are being destroyed by the quadtree, something is wrong.
        Iterator<StaticEntity> itStatic = staticEntities.iterator();
        while (itStatic.hasNext()) {
            StaticEntity e = itStatic.next();
            if (e.isToBeDestroyed()) {
                Gdx.app.log("WORLD ERROR", "A static object has been removed!");
                itStatic.remove();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Entity e : staticEntities)
            e.render(batch);
        for (Entity e : dynamicEntities)
            e.render(batch);
    }

    public Collection<DynamicEntity> getDynamicEntities() {
        return dynamicEntities;
    }

    public Collection<StaticEntity> getStaticEntities() {
        return staticEntities;
    }

    public CollisionQuadtree getQuadtree() {
        return quadtree;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
