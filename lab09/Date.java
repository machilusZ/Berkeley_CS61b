public abstract class Date {

    public abstract int dayOfYear();
    private int dayOfMonth;
    private int month;
    private int year;

    public Date(int year, int month, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    public int dayOfMonth() {
        return dayOfMonth;
    }

    public int month() {
        return month;
    }

    public int year() {
        return year;
    }

    public String toString() {
        return "" + dayOfMonth + "/" + month + "/" + year;
    }

}

