package Pair;

import java.io.FileInputStream;

public class Pair<L,R>
{
    private final L l;
    private final R r;

    public Pair(L l, R r)
    {
        this.l = l;
        this.r = r;
    }

    public L getFirst() {
        return l;
    }

    public R getSecond() {
        return r;
    }
}
