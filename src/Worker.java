public class Worker{
    private String name;
    private String position;
    private int year;

    public Worker(String name, String position, int year){
        this.name = name;
        this.position = position;
        this.year = year;
    }

    public String getName(){
        return this.name;
    }

    public String getPosition(){
        return this.position;
    }

    public int getYear(){
        return this.year;
    }

    public String toString(){
        return "ФИО работника: " + this.name +
                "\nДолжность: " + this.position +
                "\nГод поступления на работу: " + this.year;
    }
}