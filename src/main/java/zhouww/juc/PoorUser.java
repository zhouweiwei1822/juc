package zhouww.juc;

public class PoorUser extends StupidInhouseFramework {
  private  final Long density;
  private static ThreadLocal<Long> ff=new ThreadLocal<Long>();

  public PoorUser(String title, long density) {
    super(title);
    this.density = density;


  }

    private long gg(){
    return density;
    }
  public void draw() {
    long density_fudge_value = gg() + 30 * 113;
    System.out.println("draw ... " + density_fudge_value);
  }

  public static void main(String[] args) {
    StupidInhouseFramework sif = new PoorUser("Poor Me", 33244L);
    sif.draw();
  }
}