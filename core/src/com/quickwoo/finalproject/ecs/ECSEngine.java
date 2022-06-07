package com.quickwoo.finalproject.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.box2d.BodyFactory;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.ecs.systems.*;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.HeartBar;

import javax.swing.plaf.nimbus.State;

public class ECSEngine extends PooledEngine {
    private final BodyFactory bodyFactory;
    private final AssetManager assetManager;
    private final PlayerCameraSystem playerCameraSystem;
    private final Stage stage;
    private final Skin skin;
    private final static int FRAME_COlS = 2, FRAME_ROWS = 1;
    Animation<TextureRegion> slimeAnimationRight;
    Animation<TextureRegion> slimeAnimationLeft;
    Texture walkSheetRight;
    Texture walkSheetLeft;
    TextureRegion regRight;
    TextureRegion regLeft;
    float stateTime;

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

        // Add animation system
        this.addSystem(new AnimationSystem());
    }

    public void createPlayer(int x, int y, int drawOrder) {
        player = this.createEntity();
        final TextureAtlas playerAtlas = assetManager.get(AssetLoader.PLAYER_ATLAS);

        // Player
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed = 5.0f;
        player.add(playerComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x, y, 14, 21, BodyDef.BodyType.DynamicBody, true);
        player.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x, y, 0);
        transformComponent.scale.set(1, 1);
        player.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        // Create initial texture
        textureComponent.region = playerAtlas.findRegion("down", 1);
        player.add(textureComponent);

        // State
        final StateComponent stateComponent = this.createComponent(StateComponent.class);
        // First state
        stateComponent.state = StateComponent.STATE_DOWN;
        stateComponent.isLooping = true;
        player.add(stateComponent);

        // Animation
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);

        // Create animations
        Animation<TextureRegion> downAnim = new Animation<>(0.1f, playerAtlas.findRegions("down"));
        Animation<TextureRegion> upAnim = new Animation<>(0.1f, playerAtlas.findRegions("up"));
        Animation<TextureRegion> leftAnim = new Animation<>(0.1f, playerAtlas.findRegions("left"));
        Animation<TextureRegion> rightAnim = new Animation<>(0.1f, playerAtlas.findRegions("right"));

        // Add animations to intmap
        animationComponent.animations.put(StateComponent.STATE_DOWN, downAnim);
        animationComponent.animations.put(StateComponent.STATE_UP, upAnim);
        animationComponent.animations.put(StateComponent.STATE_LEFT, leftAnim);
        animationComponent.animations.put(StateComponent.STATE_RIGHT, rightAnim);
        player.add(animationComponent);

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

        //Set up the animation for the slime moving right
        walkSheetRight = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeRight.png"));

        TextureRegion[][] tmpRight = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth()/FRAME_COlS,walkSheetRight.getHeight()/FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexRight = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFrames[indexRight++] = tmpRight[i][j];
            }
        }
        slimeAnimationRight = new Animation<TextureRegion>(0.15f, walkFrames);
        regRight = slimeAnimationRight.getKeyFrame(0);

        //Set up animation for the slime moving left
        walkSheetLeft = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeLeft.png"));

        TextureRegion[][] tmpLeft = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth()/FRAME_COlS, walkSheetLeft.getHeight()/FRAME_ROWS);

        TextureRegion[] walkFramesLeft = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexLeft = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesLeft[indexLeft++] = tmpLeft[i][j];
            }
        }
        slimeAnimationLeft = new Animation<TextureRegion>(0.15f, walkFramesLeft);

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
        //State
        final StateComponent stateComponent = this.createComponent(StateComponent.class);

        // First state
        stateComponent.state = StateComponent.STATE_SLIME_LEFT;
        stateComponent.isLooping = true;
        test.add(stateComponent);

        //Animation
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.animations.put(StateComponent.STATE_SLIME_RIGHT, slimeAnimationRight);
        animationComponent.animations.put(StateComponent.STATE_SLIME_LEFT, slimeAnimationLeft);
        test.add(animationComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);
        test.add(healthComponent);

        this.addEntity(test);
    }

    public PlayerCameraSystem getCameraSystem() {
        return playerCameraSystem;
    }
}
