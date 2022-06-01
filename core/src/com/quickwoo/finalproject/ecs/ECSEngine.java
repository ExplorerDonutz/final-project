package com.quickwoo.finalproject.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.box2d.BodyFactory;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.ecs.systems.*;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.HeartBar;

public class ECSEngine extends PooledEngine {
    private final BodyFactory bodyFactory;
    private final AssetManager assetManager;
    private final PlayerCameraSystem playerCameraSystem;
    private final Stage stage;
    private final Skin skin;
    private Entity player;

    public ECSEngine(World world, FinalProject game, AssetManager assetManager, GameScreen screen, Stage stage, Skin skin) {
        this.skin = skin;
        this.stage = stage;
        bodyFactory = BodyFactory.getInstance(world);
        playerCameraSystem = new PlayerCameraSystem(game);
        this.assetManager = assetManager;
        if (FinalProject.DEBUG)
            this.addSystem(new DebugPhysicsSystem(world, game.getCamera()));

        // Add physics system
        this.addSystem(new PhysicsSystem(world));

        // Add player movement system
        this.addSystem(new PlayerMovementSystem(game.getInputManager()));

        // Add enemy movement system
        this.addSystem(new EnemyMovementSystem());

        // Add rendering system
        this.addSystem(new RenderingSystem(game, stage));

        // Add camera movement system
        this.addSystem(playerCameraSystem);
    }

    public void createPlayer(int x, int y, int drawOrder) {
        player = this.createEntity();

        // Player
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed = 10.0f;
        player.add(playerComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x, y, 16, 16, BodyDef.BodyType.DynamicBody, true);
        player.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x, y, 0);
        transformComponent.scale.set(1, 1);
        player.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        textureComponent.region = new TextureRegion((Texture) assetManager.get(AssetLoader.PLAYER_TEXTURE));
        player.add(textureComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);
        healthComponent.healthBar = new HeartBar(3, 32, 32, 0, Constants.HEIGHT - 32, skin);
        healthComponent.healthBar.setSize(256, 64);
        healthComponent.healthBar.setPosition(50,Constants.HEIGHT - 100);
        stage.addActor(healthComponent.healthBar);
        player.add(healthComponent);

        this.addEntity(player);
    }

    public void createTest(int x, int y, int drawOrder) {
        final Entity test = this.createEntity();

        // Enemy Component
        final EnemyComponent enemyComponent = this.createComponent(EnemyComponent.class);
        enemyComponent.speed = 4.0f;
        enemyComponent.player = player;
        test.add(enemyComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x, y, 16, 16, BodyDef.BodyType.DynamicBody, true);
        test.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x, y, 1);
        transformComponent.scale.set(1, 1);
        test.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        textureComponent.region = new TextureRegion((Texture) assetManager.get(AssetLoader.ENEMY_TEXTURE));
        test.add(textureComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);
        test.add(healthComponent);

        this.addEntity(test);
    }

    public PlayerCameraSystem getCameraSystem() {
        return playerCameraSystem;
    }
}
