/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp;

/**
 *
 * @author msuman
 */
public class A1Result {
    String who;
    String where;
    String why;
    String what;
    String when;

    public A1Result() {
        who = "";
        where = "";
        why = "";
        what = "";
        when = "";
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    @Override
    public String toString() {
        return ("" +
                "\n + who   -> " + who +
                "\n + what  -> " + what +
                "\n + when  -> " + when +
                "\n + where -> " + where +
                "\n + why   -> " + why +
                "");
    }
}
