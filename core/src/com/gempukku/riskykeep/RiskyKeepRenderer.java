package com.gempukku.riskykeep;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

public class RiskyKeepRenderer extends ApplicationAdapter {
    private ModelBatch batch;
    private SpriteBatch spriteBatch;
    private Camera camera;
    private Texture[] cardImages;
    private Model[] cardModels;
    private ModelInstance[] cards;
    private Texture cardBack;
    private float rotateSpeed = -0.18f;
    private float cardPositionY = 0.25f;
    private int frameCounter;

    private String font = "PT_Sans-Web-Regular.ttf";
    //private String font = "Philosopher-Regular.ttf";
//    private String font = "Tenali-Ramakrishna.ttf";
    private BitmapFont bitmapFont;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        bitmapFont = createFont();

        batch = new ModelBatch();
        camera = new PerspectiveCamera(67, 1920, 1080);
        camera.position.set(0, 0, 2f);
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.update();

        cardImages = new Texture[5];

        cardImages[0] = createTexture("/private/risky-keep/core/assets/forest.jpeg");
        cardImages[1] = createTexture("/private/risky-keep/core/assets/island.jpeg");
        cardImages[2] = createTexture("/private/risky-keep/core/assets/mountain.jpeg");
        cardImages[3] = createTexture("/private/risky-keep/core/assets/plains.jpeg");
        cardImages[4] = createTexture("/private/risky-keep/core/assets/swamp.jpeg");

        cardBack = new Texture(Gdx.files.external("/private/risky-keep/core/assets/magicCardBack.jpeg"), true);

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

    private BitmapFont createFont() {
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.external("/private/risky-keep/core/assets/" + font));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 200;
        params.color = Color.WHITE;
//        params.borderWidth = 0f;
//        params.borderColor = Color.GRAY;
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = freeTypeFontGenerator.generateFont(params);

        freeTypeFontGenerator.dispose();
        return font;
    }

    private Texture createTexture(String path) {
        return new Texture(Gdx.files.external(path));
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

//        spriteBatch.begin();
//        bitmapFont.setColor(Color.WHITE);
//        bitmapFont.draw(spriteBatch, "Risky Keep", 0,1020, 1920, Align.center, false);
//        spriteBatch.end();

        if (frameCounter < 90) {
            String frameName = String.valueOf(frameCounter + 1);
            if (frameName.length() < 2)
                frameName = "0" + frameName;
            FileHandle fh = Gdx.files.external("/private/risky-keep/core/assets/result/screen" + frameName + ".png");

            int w = 1920;
            int h = 1080;
            Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, w, h);
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
            PixmapIO.writePNG(fh, pixmap);

            pixmap.dispose();
        }

        if (frameCounter > 10 && frameCounter <= 40) {
            for (int i = 0; i < cards.length; i++) {
                float factor = i - (1f * cards.length - 1) / 2;
                cards[i].transform.translate(0, -5, 0);
                cards[i].transform.rotate(new Vector3(0, 0, 1), factor * rotateSpeed);
                cards[i].transform.translate(0, 5, 0);
            }
        }

        frameCounter++;
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        batch.dispose();
        for (Texture cardImage : cardImages) {
            cardImage.dispose();
        }
        cardBack.dispose();
        bitmapFont.dispose();
    }
}
