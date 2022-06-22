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
import com.quickwoo.finalproject.Constants;
import com.quickwoo.finalproject.FinalProject;
import com.quickwoo.finalproject.box2d.BodyFactory;
import com.quickwoo.finalproject.ecs.components.*;
import com.quickwoo.finalproject.ecs.systems.*;
import com.quickwoo.finalproject.loader.AssetLoader;
import com.quickwoo.finalproject.map.GameObject;
import com.quickwoo.finalproject.screens.GameScreen;
import com.quickwoo.finalproject.screens.HeartBar;

public class ECSEngine extends PooledEngine {
    private final BodyFactory bodyFactory;
    private final AssetManager assetManager;
    private final PlayerCameraSystem playerCameraSystem;
    private final CollisionSystem collisionSystem;
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
        collisionSystem = new CollisionSystem(world, this, game);
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
        this.addSystem(new RenderingSystem(game));

        // Add camera movement system
        this.addSystem(playerCameraSystem);

        // Add animation system
        this.addSystem(new AnimationSystem());

        // Add collision system
        this.addSystem(collisionSystem);

        // Add combat system
        this.addSystem(new PlayerCombatSystem(game.getInputManager(), game, this));

        // Interaction system
        this.addSystem(new InteractionSystem(game, this, screen));
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
        box2DComponent.reset();
        box2DComponent.body = bodyFactory.makeBox(x, y, 6, 14, 1.0f, BodyDef.BodyType.DynamicBody, true, false);
        box2DComponent.body.setUserData(player);
        box2DComponent.attackBody = bodyFactory.makeBox(x, y, 24, 24, 1.0f, BodyDef.BodyType.DynamicBody, true, true);
        box2DComponent.attackBody.setUserData(player);
        player.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.reset();
        transformComponent.position.set(x, y, drawOrder);
        transformComponent.scale.set(1, 1);
        player.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        // Create initial texture
        textureComponent.region = playerAtlas.findRegion("down", 1);
        textureComponent.isDrawn = true;
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

        //Animation Variables
        final int FRAME_COlS = 7, FRAME_ROWS = 1;
        float frameSpeed = 0.05f;
        Animation<TextureRegion> attackUp;
        Animation<TextureRegion> attackDown;
        Animation<TextureRegion> attackLeft;
        Animation<TextureRegion> attackRight;
        Texture walkSheetUp;
        Texture walkSheetDown;
        Texture walkSheetLeft;
        Texture walkSheetRight;

        //Attack animation Down
        walkSheetDown = assetManager.get(AssetLoader.PLAYER_ATTACK_DOWN);
        TextureRegion[][] tmpDown = TextureRegion.split(walkSheetDown, walkSheetDown.getWidth() / FRAME_COlS, walkSheetDown.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFramesDown = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexDown = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesDown[indexDown++] = tmpDown[i][j];
            }
        }
        attackDown = new Animation<>(frameSpeed, walkFramesDown);

        //Attack animation up
        walkSheetUp = assetManager.get(AssetLoader.PLAYER_ATTACK_UP);
        TextureRegion[][] tmpUp = TextureRegion.split(walkSheetUp, walkSheetUp.getWidth() / FRAME_COlS, walkSheetUp.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFramesUp = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexUp = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesUp[indexUp++] = tmpUp[i][j];
            }
        }
        attackUp = new Animation<>(frameSpeed, walkFramesUp);

        //Attack animation left
        walkSheetLeft = assetManager.get(AssetLoader.PLAYER_ATTACK_LEFT);
        TextureRegion[][] tmpLeft = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth() / FRAME_COlS, walkSheetLeft.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFramesLeft = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexLeft = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesLeft[indexLeft++] = tmpLeft[i][j];
            }
        }
        attackLeft = new Animation<>(frameSpeed, walkFramesLeft);

        //Attack animation right
        walkSheetRight = assetManager.get(AssetLoader.PLAYER_ATTACK_RIGHT);
        TextureRegion[][] tmpRight = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth() / FRAME_COlS, walkSheetRight.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFramesRight = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexRight = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesRight[indexRight++] = tmpRight[i][j];
            }
        }
        attackRight = new Animation<>(frameSpeed, walkFramesRight);

        // Add animations to intmap
        animationComponent.animations.put(StateComponent.STATE_DOWN, downAnim);
        animationComponent.animations.put(StateComponent.STATE_UP, upAnim);
        animationComponent.animations.put(StateComponent.STATE_LEFT, leftAnim);
        animationComponent.animations.put(StateComponent.STATE_RIGHT, rightAnim);
        animationComponent.animations.put(StateComponent.STATE_ATTACK_UP, attackUp);
        animationComponent.animations.put(StateComponent.STATE_ATTACK_DOWN, attackDown);
        animationComponent.animations.put(StateComponent.STATE_ATTACK_LEFT, attackLeft);
        animationComponent.animations.put(StateComponent.STATE_ATTACK_RIGHT, attackRight);
        player.add(animationComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);

        if (FinalProject.DEBUG) {
            healthComponent.healthBar = new HeartBar(69420, 32, 32, 0, Constants.HEIGHT - 32, skin);
            healthComponent.health = 69420;
        } else
            healthComponent.healthBar = new HeartBar(3, 32, 32, 0, Constants.HEIGHT - 32, skin);

        healthComponent.healthBar.setSize(256, 64);
        healthComponent.healthBar.setPosition(50, Constants.HEIGHT - 100);
        stage.addActor(healthComponent.healthBar);
        player.add(healthComponent);

        // Battle
        final BattleComponent battleComponent = this.createComponent(BattleComponent.class);
        player.add(battleComponent);

        // Collision
        final CollisionComponent collisionComponent = this.createComponent(CollisionComponent.class);
        player.add(collisionComponent);

        this.addEntity(player);
    }

    public void createSlime(int x, int y, int drawOrder) {
        final Entity slime = this.createEntity();

        //Set up the animation for the slime moving right
        walkSheetRight = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeRight.png"));

        TextureRegion[][] tmpRight = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth() / FRAME_COlS, walkSheetRight.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexRight = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFrames[indexRight++] = tmpRight[i][j];
            }
        }
        slimeAnimationRight = new Animation<>(0.15f, walkFrames);
        regRight = slimeAnimationRight.getKeyFrame(0);

        //Set up animation for the slime moving left
        walkSheetLeft = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeLeft.png"));

        TextureRegion[][] tmpLeft = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth() / FRAME_COlS, walkSheetLeft.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFramesLeft = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexLeft = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesLeft[indexLeft++] = tmpLeft[i][j];
            }
        }
        slimeAnimationLeft = new Animation<>(0.15f, walkFramesLeft);

        // Enemy Component
        final EnemyComponent enemyComponent = this.createComponent(EnemyComponent.class);
        enemyComponent.speed = 2.0f;
        enemyComponent.player = player;
        slime.add(enemyComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x, y, 16, 16, 0.1f, BodyDef.BodyType.DynamicBody, true, false);
        box2DComponent.body.setUserData(slime);
        slime.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x, y, drawOrder);
        transformComponent.scale.set(1, 1);
        slime.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        textureComponent.region = new TextureRegion((Texture) assetManager.get(AssetLoader.ENEMY_TEXTURE));
        textureComponent.isDrawn = true;
        slime.add(textureComponent);
        //State
        final StateComponent stateComponent = this.createComponent(StateComponent.class);

        // First state
        stateComponent.state = StateComponent.STATE_SLIME_LEFT;
        stateComponent.isLooping = true;
        slime.add(stateComponent);

        //Animation
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.animations.put(StateComponent.STATE_SLIME_RIGHT, slimeAnimationRight);
        animationComponent.animations.put(StateComponent.STATE_SLIME_LEFT, slimeAnimationLeft);
        slime.add(animationComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);
        slime.add(healthComponent);

        this.addEntity(slime);
    }

    public void createGameObject(GameObject gameObject) {
        final Entity entity = this.createEntity();

        // Game Object
        final GameObjectComponent gameObjectComponent = this.createComponent(GameObjectComponent.class);
        gameObjectComponent.type = gameObject.getType();
        if (gameObject.getType() == GameObjectComponent.TYPE_TELEPORT) {
            gameObjectComponent.playerLoc = gameObject.getPlayerLoc();
            gameObjectComponent.map = gameObject.getNextMap();
        }
        entity.add(gameObjectComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        switch (gameObjectComponent.type) {
            case GameObjectComponent.TYPE_TELEPORT:
                box2DComponent.body = bodyFactory.makeBox((gameObject.getX() + (gameObject.getRegion().getRegionWidth() / 2f)) * 2, (gameObject.getY() + (gameObject.getRegion().getRegionHeight() / 2f)) * 2, gameObject.getWidth(), gameObject.getHeight(), 1.0f, BodyDef.BodyType.StaticBody, true, false);
                break;
            case GameObjectComponent.TYPE_SIGN:
                box2DComponent.body = bodyFactory.makeBox((gameObject.getX() + (gameObject.getRegion().getRegionWidth() / 2f)) * 2, (gameObject.getY() + (gameObject.getRegion().getRegionHeight() / 2f)) * 2, gameObject.getWidth() * 2, gameObject.getHeight() * 2, 1.0f, BodyDef.BodyType.StaticBody, true, true);
        }
        box2DComponent.body.setUserData(entity);
        entity.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.scale.set(1, 1);
        transformComponent.position.set(gameObject.getX(), gameObject.getY(), 1);
        entity.add(transformComponent);

        //Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        textureComponent.region = gameObject.getRegion();
        switch (gameObjectComponent.type) {
            case GameObjectComponent.TYPE_TELEPORT:
                textureComponent.isDrawn = false;
                break;
            case GameObjectComponent.TYPE_SIGN:
                textureComponent.isDrawn = true;
        }
        entity.add(textureComponent);

        // Collision
        final CollisionComponent collisionComponent = this.createComponent(CollisionComponent.class);
        entity.add(collisionComponent);

        this.addEntity(entity);
    }

    public void createBoss(int x, int y, int drawOrder) {
        final Entity slime = this.createEntity();

        //Set up the animation for the slime moving right
        walkSheetRight = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeRight.png"));

        TextureRegion[][] tmpRight = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth() / FRAME_COlS, walkSheetRight.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexRight = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFrames[indexRight++] = tmpRight[i][j];
            }
        }
        slimeAnimationRight = new Animation<>(0.15f, walkFrames);
        regRight = slimeAnimationRight.getKeyFrame(0);

        //Set up animation for the slime moving left
        walkSheetLeft = new Texture(Gdx.files.internal("RPG Sprites/sprSlimeLeft.png"));

        TextureRegion[][] tmpLeft = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth() / FRAME_COlS, walkSheetLeft.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFramesLeft = new TextureRegion[FRAME_COlS * FRAME_ROWS];
        int indexLeft = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COlS; j++) {
                walkFramesLeft[indexLeft++] = tmpLeft[i][j];
            }
        }
        slimeAnimationLeft = new Animation<>(0.15f, walkFramesLeft);

        // Enemy Component
        final EnemyComponent enemyComponent = this.createComponent(EnemyComponent.class);
        enemyComponent.speed = 2.0f;
        enemyComponent.player = player;
        enemyComponent.isBoss = true;
        slime.add(enemyComponent);

        // Box2D
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        box2DComponent.body = bodyFactory.makeBox(x, y, 32, 32, 0.1f, BodyDef.BodyType.DynamicBody, true, false);
        box2DComponent.body.setUserData(slime);
        slime.add(box2DComponent);

        // Transform
        final TransformComponent transformComponent = this.createComponent(TransformComponent.class);
        transformComponent.position.set(x, y, drawOrder);
        transformComponent.scale.set(2, 2);
        slime.add(transformComponent);

        // Texture
        final TextureComponent textureComponent = this.createComponent(TextureComponent.class);
        textureComponent.region = new TextureRegion((Texture) assetManager.get(AssetLoader.ENEMY_TEXTURE));
        textureComponent.isDrawn = true;
        slime.add(textureComponent);
        //State
        final StateComponent stateComponent = this.createComponent(StateComponent.class);

        // First state
        stateComponent.state = StateComponent.STATE_SLIME_LEFT;
        stateComponent.isLooping = true;
        slime.add(stateComponent);

        //Animation
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.animations.put(StateComponent.STATE_SLIME_RIGHT, slimeAnimationRight);
        animationComponent.animations.put(StateComponent.STATE_SLIME_LEFT, slimeAnimationLeft);
        slime.add(animationComponent);

        // Health
        final HealthComponent healthComponent = this.createComponent(HealthComponent.class);
        healthComponent.health = 10;
        slime.add(healthComponent);

        this.addEntity(slime);
    }

    public PlayerCameraSystem getCameraSystem() {
        return playerCameraSystem;
    }

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
    }

    public Entity getPlayer() {
        return player;
    }


}
