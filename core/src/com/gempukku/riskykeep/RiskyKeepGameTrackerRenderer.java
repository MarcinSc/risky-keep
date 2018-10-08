package com.gempukku.riskykeep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class RiskyKeepGameTrackerRenderer extends SizedApplicationAdapter {
    private ModelBatch batch;
    private Camera camera;
    private Texture[] cardImages;
    private Model[] cardModels;
    private ModelInstance[] cards;
    private Texture cardBack;
    private float maxRotation = -0.18f * 30;
    private float cardPositionY = 0.25f;

    private static final String ASSET_PATH = "/Users/marcin.sciesinski/private/risky-keep/core/assets/";
    private static final String OUTPUT_PATH = "/Users/marcin.sciesinski/private/risky-keep/core/out/result/";

    @Override
    public int getDesiredWidth() {
        return 320;
    }

    @Override
    public int getDesiredHeight() {
        return 180;
    }

    @Override
    public void create() {

        batch = new ModelBatch();
        camera = new PerspectiveCamera(67, 1920, 1080);
        camera.position.set(0, 0, 2f);
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.update();

        cardImages = new Texture[5];

        cardImages[0] = createTexture(ASSET_PATH + "forest.jpeg");
        cardImages[1] = createTexture(ASSET_PATH + "island.jpeg");
        cardImages[2] = createTexture(ASSET_PATH + "mountain.jpeg");
        cardImages[3] = createTexture(ASSET_PATH + "plains.jpeg");
        cardImages[4] = createTexture(ASSET_PATH + "swamp.jpeg");

        cardBack = createTexture(ASSET_PATH + "magicCardBack.jpeg");

        cardModels = new Model[cardImages.length];
        for (int i = 0; i < cardModels.length; i++) {
            cardModels[i] = createCard(cardImages[i]);
        }
        cards = new ModelInstance[7];
        cards[0] = new ModelInstance(cardModels[0]);
        cards[0].transform.translate(0, cardPositionY, 0f);
        cards[1] = new ModelInstance(cardModels[1]);
        cards[1].transform.translate(0, cardPositionY, -0.01f);
        cards[2] = new ModelInstance(cardModels[2]);
        cards[2].transform.translate(0, cardPositionY, -0.02f);
        cards[3] = new ModelInstance(cardModels[0]);
        cards[3].transform.translate(0, cardPositionY, -0.03f);
        cards[4] = new ModelInstance(cardModels[4]);
        cards[4].transform.translate(0, cardPositionY, -0.04f);
        cards[5] = new ModelInstance(cardModels[1]);
        cards[5].transform.translate(0, cardPositionY, -0.05f);
        cards[6] = new ModelInstance(cardModels[3]);
        cards[6].transform.translate(0, cardPositionY, -0.06f);
    }

    private Texture createTexture(String path) {
        return new Texture(Gdx.files.absolute(path));
    }

    private Model createCard(Texture frontTexture) {
        float width = 0.7163f;
        Material material = new Material(TextureAttribute.createDiffuse(frontTexture), new BlendingAttribute());
        ModelBuilder modelBuilder = new ModelBuilder();
        int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates;
        modelBuilder.begin();
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, material)
                .rect(-width / 2, -0.5f, 0, width / 2, -0.5f, 0, width / 2, 0.5f, 0, -width / 2, 0.5f, 0, 0, 0, 1);
        return modelBuilder.end();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin(camera);
        for (ModelInstance card : cards) {
            batch.render(card);
        }
        batch.end();

        long phase = System.currentTimeMillis() % 10000;
        float angleMultiplier = 0;
        if (2000 <= phase && phase < 5000)
            angleMultiplier = (phase - 2000) / 3000f;
        else if (5000 <= phase && phase < 7000)
            angleMultiplier = 1f;
        else if (7000 <= phase && phase < 10000)
            angleMultiplier = 1f - (phase - 7000) / 3000f;

        for (int i = 0; i < cards.length; i++) {
            float factor = i - (1f * cards.length - 1) / 2;
            cards[i].transform = new Matrix4();
            cards[i].transform.translate(0, -5, 0.01f * i);
            cards[i].transform.rotate(new Vector3(0, 0, 1), factor * angleMultiplier * maxRotation);
            cards[i].transform.translate(0, 5.25f, 0);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture cardImage : cardImages) {
            cardImage.dispose();
        }
        cardBack.dispose();
    }
}
