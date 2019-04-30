package group1.project.data.queries;

/**
 * @author Kevin Troisi
 */
public enum ElementOrder {
    ASCENDING("ASC"), DESCENDING("DESC");

    private String sqlEquivalent;

    ElementOrder(String sqlEquivalent){
        this.sqlEquivalent = sqlEquivalent;
    }

    @Override
    public String toString(){
        return sqlEquivalent;
    }
}
