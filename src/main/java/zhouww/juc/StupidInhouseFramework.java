package zhouww.juc;

/**
 * 大萝卜:
 * https://blog.csdn.net/wangzhangxing/article/details/84597125
 *
 * 大萝卜:
 * https://www.javaspecialists.eu/archive/Issue164-Why-0x61c88647.html
 *
 * 大萝卜:
 * https://www.javaspecialists.eu/archive/
 */
public abstract class StupidInhouseFramework {
  private final String title;

  protected StupidInhouseFramework(String title) {
    this.title = title;
    draw();
  }

  public abstract void draw();

  public String toString() {
    return "StupidInhouseFramework " + title;
  }
}
  