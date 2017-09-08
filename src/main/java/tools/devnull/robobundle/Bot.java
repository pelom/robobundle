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

package tools.devnull.robobundle;

import tools.devnull.robobundle.calc.Point;
import tools.devnull.robobundle.parts.Body;
import tools.devnull.robobundle.parts.Gun;
import tools.devnull.robobundle.parts.Radar;

/**
 * Interface that defines a robot.
 * <p/>
 * In Robobundle, every robot has three independent parts: a {@link Gun}, a {@link Body}
 * and a {@link Radar}. The bot has also a pluggable system and a event driven module
 * designed for better composition of features.
 *
 * @author Marcelo Guimarães
 * @see tools.devnull.robobundle.annotation.When
 */
public interface Bot extends Localizable {

  /**
   * @return the robot's name.
   */
  String name();

  /**
   * Returns a coordinate representing this robot location in the battlefield.
   *
   * @return a coordinate representing this robot location in the battlefield.
   */
  Point location();

  /**
   * Returns the robot's gun part.
   *
   * @return the robot's gun
   */
  Gun gun();

  /**
   * The robot's body part.
   *
   * @return the robot's body
   */
  Body body();

  /**
   * Returns the robot's radar part
   *
   * @return the robot's radar
   */
  Radar radar();

  /**
   * Logs a message in the robot's output stream
   *
   * @param message the message to log
   * @param params  the parameters to the message (if it is a format)
   */
  void log(Object message, Object... params);

  /**
   * Logs an exception in the robot's output stream
   *
   * @param throwable the exception to log
   */
  void log(Throwable throwable);

  /**
   * Plugs a component that will listen to events through methods annotated
   * with {@link tools.devnull.robobundle.annotation.When}.
   *
   * @return the given plugin
   * @see #broadcast(String, Object...)
   */
  <E> E plug(E plugin);

  /**
   * Sends an event to the listeners
   *
   * @param eventName the event name
   * @param args      the event parameters
   */
  void broadcast(String eventName, Object... args);

  /**
   * Gets the statistics for this bot
   *
   * @return the statistics for this bot.
   */
  BotStatistics statistics();
}
