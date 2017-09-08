/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package tools.devnull.robobundle.plugin;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.EnemyData;
import tools.devnull.robobundle.annotation.When;
import tools.devnull.robobundle.condition.Condition;
import tools.devnull.robobundle.event.EnemyScannedEvent;
import tools.devnull.robobundle.event.Events;
import tools.devnull.robobundle.util.Drawer;

import java.util.*;

import static tools.devnull.robobundle.util.Drawer.Mode.TRANSPARENT;
import static java.awt.Color.LIGHT_GRAY;

/**
 * @author Marcelo Guimarães
 */
public class EnemyTracker {

  private Map<String, List<Enemy>> enemyData;

  private int historySize;

  private final Bot bot;

  public EnemyTracker(Bot bot) {
    this(bot, 20);
  }

  public EnemyTracker(Bot bot, int historySize) {
    this.enemyData = new HashMap<>();
    this.bot = bot;
    this.historySize = historySize;
  }

  public Condition enemyIsTurning() {
    return () -> dataFor(bot.radar().target()).isTurning();
  }

  @When(Events.ENEMY_SCANNED)
  public void registerEnemy(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    if (!enemyData.containsKey(enemy.name())) {
      enemyData.put(enemy.name(), new LinkedList<Enemy>());
    }
    List<Enemy> history = enemyData.get(enemy.name());
    history.add(enemy);
    if (history.size() > historySize) {
      history.remove(0);
    }
  }

  public EnemyData dataFor(final Enemy enemy) {
    return () -> {
      if (enemy == null || !enemyData.containsKey(enemy.name())) {
        return Collections.emptyList();
      }
      return new ArrayList<>(enemyData.get(enemy.name()));
    };
  }

  @When(Events.DRAW)
  public void drawHistory(Drawer drawer) {
    Collection<Enemy> enemies = bot.radar().knownEnemies();
    for (Enemy enemy : enemies) {
      int i = 0;
      List<Enemy> history = dataFor(enemy).history();
      Collections.reverse(history);
      for (Enemy enemyHistory : history) {
        drawer.draw(TRANSPARENT, LIGHT_GRAY).circle().at(enemyHistory.location());
        if (++i == 10) {
          break;
        }
      }
    }
  }

  public boolean isEnemyStopped(Enemy enemy) {
    for (Enemy enemyHistory : dataFor(enemy).history()) {
      if (enemyHistory.isMoving()) {
        return false;
      }
    }
    return true;
  }

  public Condition targetStoped() {
    return () -> {
      if (bot.radar().hasTargetSet()) {
        Enemy target = bot.radar().target();
        return isEnemyStopped(target);
      }
      return false;
    };
  }

}
