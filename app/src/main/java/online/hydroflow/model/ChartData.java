package online.hydroflow.model;

public class ChartData {

    // alt + insert to constructors, getters and setters

    String timeStamp;
    String chartDataNow;

    public ChartData() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getChartDataNow() {
        return chartDataNow;
    }

    public ChartData(String timeStamp, String chartDataNow) {

        this.timeStamp = timeStamp;
        this.chartDataNow = chartDataNow;
    }


}
