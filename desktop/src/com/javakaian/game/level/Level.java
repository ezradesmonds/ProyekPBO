package com.javakaian.game.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.controllers.EnemyController;
import com.javakaian.game.controllers.TowerController;
import com.javakaian.game.map.Grid;
import com.javakaian.game.map.Grid.EnumGridType;
import com.javakaian.game.map.Map;
import com.javakaian.game.ui.menu.InformationMenu;
import com.javakaian.game.ui.menu.MainControlMenu;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.states.PlayState;
import com.javakaian.game.states.State.StateEnum;
import com.javakaian.game.towers.BaseTower;
import com.javakaian.game.towers.BaseTower.TowerType;
import com.javakaian.game.util.GameConstants;
import com.javakaian.game.util.GameUtils;

public class Level {

    private EnemyController enemyController;
    private TowerController towerController;
    //asosiasi delegasi tugas

    private Map map;
    private MainControlMenu towerSelectionMenu;
    private InformationMenu informationMenu;

    private int enemyNumber;
    private int enemyHealth;
    private int score;
    private int money;
    private int remainingHealth;
    private final BitmapFont bitmapFont;
    private final PlayState state;

    private int timeLeft;
    private boolean renderTimeAndWaveNumber = false;
    private int waveNumber;

    public Level(PlayState state) {
        this.state = state;
        this.bitmapFont = GameUtils.generateBitmapFont(80, Color.BLACK);
        init();
    }

    private void init() {
        waveNumber = 1;
        score = 0;
        money = GameConstants.INITIAL_MONEY;
        enemyNumber = 10;
        enemyHealth = 200;
        remainingHealth = GameConstants.REMAINING_HEALTH;

        map = new Map();
        enemyController = new EnemyController(this);
        towerController = new TowerController();
        informationMenu = new InformationMenu(MyAtlas.MENU_TILE);
        towerSelectionMenu = new MainControlMenu(this);
    }

    public void sellSelectedTower() {
        BaseTower towerToSell = towerController.getSelectedTower();
        if (towerToSell != null) {
            Grid grid = map.getSelectedGrid(towerToSell.center.x, towerToSell.center.y);
            if (grid != null && grid.getType() == EnumGridType.TOWER) {
                int sellValue = towerToSell.getTotalCost();
                increaseMoney(sellValue);

                towerController.removeTower(towerToSell);
                grid.setType(EnumGridType.LAND);

                informationMenu.clearInformations();
                towerSelectionMenu.clearSelectedTower();
            }
        }
    }

    public void render(ShapeRenderer sr) {
        map.render(sr);
        enemyController.render(sr);
        towerController.render(sr);
        towerSelectionMenu.render(sr);
    }

    public void render(SpriteBatch sb) {
        map.render(sb);
        enemyController.render(sb);
        towerController.render(sb);
        towerSelectionMenu.render(sb);
        informationMenu.render(sb);
        if (renderTimeAndWaveNumber) {
            GameUtils.renderCenter(
                    "Wave: " + waveNumber + " in: " + timeLeft + " second", sb,
                    bitmapFont);
        }
    }

    public void update(float deltaTime) {
        map.update(deltaTime);
        enemyController.update(deltaTime);
        towerController.update(deltaTime);
    }

    public void updateInputs(float x, float y) {
        towerSelectionMenu.updateInputs(x, y);
    }

    public void createTowerClicked(float x, float y, TowerType type) {

        Grid grid = map.getSelectedGrid(x, y);
        if (grid == null)
            return;
        switch (grid.getType()) {
            case TOWER:
                System.out.println("CAN NOT BUILD TOWER ALREADY EXIST");
                break;
            case LAND:
                int cost = towerController.buildTower(grid.getPosition().x, grid.getPosition().y,
                        enemyController.getEnemyList(), type, money);
                if (cost != 0) {
                    grid.setType(EnumGridType.TOWER);
                    decreaseMoney(cost);
                }
                this.map.getBoard().setRender(false);
                break;
            case PATH:
                System.out.println("CAN NOT BUILD TO THE PATH");
                break;
        }
    }

    public void enemyPassedTheCheckPoint() {
        remainingHealth--;
        towerSelectionMenu.fireHealthChanged(remainingHealth);
        if (remainingHealth == 0) {
            state.gameOver();
        }
    }

    public void enemyKilled(int bounty) {

        score += GameConstants.SCORE_INCREASE_CONSTANT;
        enemyNumber -= 1;
        increaseMoney(bounty);
        informationMenu.fireScoreChanged(this.score);
        towerSelectionMenu.fireEnemyNumberChanged(enemyNumber);
    }

    public void newWaveCreated(int size) {
        waveNumber++;
        enemyNumber = size;
        renderTimeAndWaveNumber = false;
    }

    public void touchDown(float x, float y) {

        if (towerSelectionMenu.contains(x, y)) {
            towerSelectionMenu.touchDown(x, y);
        } else {
            selectGrid(x, y);
        }

    }

    public void touchRelease(float x, float y) {
        towerSelectionMenu.touchRelease(x, y);
    }

    public void selectGrid(float x, float y) {

        Grid grid = this.map.getSelectedGrid(x, y);
        if (grid == null)
            return;
        switch (grid.getType()) {
            case TOWER:
                BaseTower t = towerController.getSelectedTower(grid.getPosition());
                informationMenu.updateTowerInformation(t);
                towerSelectionMenu.updateUpgradeButtons(money);
                towerSelectionMenu.onTowerSelectionChanged(true);
                break;
            case LAND:
                towerController.clearSelectedTower();
                informationMenu.clearInformations();
                towerSelectionMenu.clearSelectedTower();
                towerSelectionMenu.onTowerSelectionChanged(false);
                break;
            case PATH:
                towerController.clearSelectedTower();
                towerSelectionMenu.clearSelectedTower();
                towerSelectionMenu.onTowerSelectionChanged(false);
                break;

            default:
                break;

        }

    }

    public BaseTower getSelectedTower() {
        return towerController.getSelectedTower();
    }

    public Map getMap() {
        return map;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public int getEnemyNumber() {
        return enemyNumber;
    }

    public void renderGrids(boolean b) {
        this.map.getBoard().setRender(b);
    }

    public void nextWaveCountDown(int i) {
        this.timeLeft = i;
        renderTimeAndWaveNumber = true;

    }

    public void increaseAttackClickled() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getAttackPrice();
        if (cost <= money) {
            towerController.increaseAttack();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }

    }

    public void increaseRangeClicked() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getRangePrice();
        if (cost <= money) {
            towerController.increaseRange();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }

    }

    public void increaseSpeedClicked() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getSpeedPrice();
        if (cost <= money) {
            towerController.increaseSpeed();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }
    }

    public void restart() {
        init();
    }

    public void pause() {
        state.pause();
    }

    public void resume() {
        state.resume();
    }

    public void speed2xClicked() {
        towerController.speed2xClicked();
        enemyController.speed2xClicked();
    }

    public void normalSpeedClicked() {
        towerController.normalSpeedClicked();
        enemyController.normalSpeedClicked();
    }

    public void increaseMoney(int amount) {
        this.money += amount;
        informationMenu.fireMoneyChanged(money);
        towerSelectionMenu.moneyChanged(money);
    }

    public void decreaseMoney(int amount) {
        this.money -= amount;
        informationMenu.fireMoneyChanged(money);
        towerSelectionMenu.moneyChanged(money);
    }

}
