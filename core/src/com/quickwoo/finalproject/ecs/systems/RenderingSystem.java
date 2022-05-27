package com.quickwoo.finalproject.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.ecs.Mapper;
import com.quickwoo.finalproject.ecs.components.Box2DComponent;
import com.quickwoo.finalproject.ecs.components.HealthComponent;
import com.quickwoo.finalproject.ecs.components.TextureComponent;
import com.quickwoo.finalproject.ecs.components.TransformComponent;

import java.util.Comparator;


public class RenderingSystem extends SortedIteratingSystem {
   private final static String TAG = RenderingSystem.class.getSimpleName();
    private final ComponentMapper<TextureComponent> texComponent;
   private final ComponentMapper<TransformComponent> transformComponent;
   private final ComponentMapper<HealthComponent> healthComponent;
   private final Comparator<Entity> comparator;
   private final Array<Entity> renderQueue;
   private final SpriteBatch batch;
   private final Stage stage;

    public RenderingSystem(FinalProject game, Stage stage) {
        super(Family.all(TextureComponent.class).get(), new ZComparator());
        texComponent = Mapper.textureMapper;
        transformComponent = Mapper.transformMapper;
        healthComponent = Mapper.healthMapper;
        comparator = new ZComparator();
        renderQueue = new Array<>();
        batch = game.getBatch();
        this.stage = stage;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Sort the render queue based on the z coordinate
        renderQueue.sort(comparator);


        batch.begin();

        //Loop through each entity
        for (Entity entity : new Array.ArrayIterator<>(renderQueue)) {
            TextureComponent tex = texComponent.get(entity);
            TransformComponent transform = transformComponent.get(entity);
            if (tex.region == null) {
                Gdx.app.log(TAG, "Texture region for entity " + entity + " was null!");
                continue;
            }

            float texWidth = tex.region.getRegionWidth();
            float texHeight = tex.region.getRegionHeight();

            float originX = texWidth / 2f;
            float originY = texHeight / 2f;

            batch.draw(tex.region, transform.position.x - originX, transform.position.y - originY, originX, originY, texWidth, texHeight, PixelsToMeters(transform.scale.x), PixelsToMeters(transform.scale.y), transform.rotation);

        }
        batch.end();
        renderQueue.clear();
    }

    // Method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * Constants.PIXELS_TO_METERS;
    }
}
