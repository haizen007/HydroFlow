package online.hydroflow.model;

public class Model {

    // alt + insert to constructors, getters and setters

    long timeStamp;
    String consumoAgora;

    public Model() {
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getConsumoAgora() {
        return consumoAgora;
    }

    public Model(long timeStamp, String consumoAgora) {

        this.timeStamp = timeStamp;
        this.consumoAgora = consumoAgora;
    }


}
