public class MathServiceImpl implements MathService{
    public float do_add(float a, float b) {
        return a + b;
    }

    public float do_sqrt(float a) {
        return (float) Math.sqrt(a);
    }
}
