package org.cloudbus.cloudsim.mkg;

abstract class f_xj {
    // single function  multi independent variable
    // a single value is returned indiced to equation_ref
    // example f[0]=x[0]+sin(x[1])
    //         f[1]=x[0]*x[0]-x[1]
    // func(x,1) returns the value of f[1]
    // func(x,0) returns the value of f[0]

    abstract double func(double x[]);
}

public class firefly {
    double gamma, alpha, beta;
    double alpha0, alphan;
    int m;
    int n;
    // m number of fireflies
// n number of variables for each firefly 
// I0 light intensity at source
// gamma absorbtion coefficient
// alpha size of the random step
// beta attractiveness
    double x[][];
    double I0[];
    double ymin;
    double xmin[];
    double xmax[];
    double BEST[];
    int imin;
    int ngeneration;

    public firefly(f_xj ff, int mi, double xxmin[], double xxmax[], double gammai, double alpha0i, double alphani, int ngenerationi) {
//initial population set
        n = xxmin.length;
        m = mi;
        x = new double[m][n];
        I0 = new double[m];
        xmin = new double[n];

        gamma = gammai;
        alpha0 = alpha0i;
        alphan = alphani;
        ngeneration = ngenerationi;
        BEST = new double[ngeneration];
        ymin = 1.0e50;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = xxmin[j] + Math.random() * (xxmax[j] - xxmin[j]);
            }

            I0[i] = ff.func(x[i]);
            if (I0[i] < ymin) {
                ymin = I0[i];
                imin = i;
                for (int k = 0; k < n; k++) {
                    xmin[k] = x[i][k];
                }
                alpha = alpha0;
            }
            ;
        }
//end of init
        int l = 0;
        double d = 0;
        double t = 0;
        while (l < ngeneration) {

            double fi = 0, fj = 0;
            for (int i = 0; i < m; i++) {
                fi = ff.func(x[i]);
                for (int j = 0; j < m; j++) {
                    fj = ff.func(x[j]);
                    if (fi < fj) {
                        beta = attractiveness(i, j);
                        for (int k = 0; k < n; k++) {   //The best one will remain the rest will change
                            if (x[i][k] != xmin[k]) {
                                x[i][k] = (1.0 - beta) * x[i][k] + beta * x[j][k] + alpha * (Math.random() - 0.5);
                            }
                        }
                    } else {
                        for (int k = 0; k < n; k++) {
                            x[i][k] = x[i][k] + alpha * (Math.random() - 0.5);
                        }
                    }
                }
                fi = ff.func(x[i]);
                I0[i] = fi;
                if (I0[i] < ymin) {
                    ymin = I0[i];
                    imin = i;
                    for (int k = 0; k < n; k++) {
                        xmin[k] = x[i][k];
                    }
                }
                ;
            }
//best firefly moves randomly
            for (int k = 0; k < n; k++) {
                x[imin][k] = x[imin][k] + alpha * (Math.random() - 0.5);
            }
            I0[imin] = ff.func(x[imin]);
            if (I0[imin] < ymin) {
                ymin = I0[imin];
            }
            alpha = alphan + (alpha0 - alphan) * Math.exp(-t);
            t = 0.1 * l;

            BEST[l] = ymin;
            l++;
        }

    }

    public double distance(int i, int j) {
        double d = 0;
        for (int k = 0; k < n; k++) {
            d += (x[i][k] - x[j][k]) * (x[i][k] - x[j][k]);
        }
        d = Math.sqrt(d);
        return d;
    }

    public double attractiveness(int i, int j) {
        double d = distance(i, j);
        return I0[i] / (1 + gamma * d * d);
        //return I0[i]*Math.exp(-gamma*d*d);
    }

    public String toString() {
        String s = "ymin = " + ymin + "\nxmin = ";
        for (int k = 0; k < n; k++) {
            s += xmin[k] + " ";
        }
        return s;
    }

    public String toString(f_xj ff, double xx[]) {
        String s = "ymin = " + ff.func(xx) + "\nxmin = ";
        for (int k = 0; k < n; k++) {
            s += xx[k] + " ";
        }
        return s;
    }

    public double[] xmin() {
        return xmin;
    }

    double[][] solution() {

        double out[][] = new double[2][xmin.length];
        out[0][0] = ymin;
        for (int i = 0; i < xmin.length; i++) {
            out[1][i] = xmin[i];
        }
        return out;

    }


    void toStringnew() {
        double[][] out = solution();
        System.out.println("Optimized value = " + out[0][0]);
        for (int i = 0; i < xmin.length; i++) {
            System.out.println("x[" + i + "] = " + out[1][i]);
        }

    }


}