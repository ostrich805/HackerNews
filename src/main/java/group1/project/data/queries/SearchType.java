package group1.project.data.queries;

/**
 * @author Kevin Troisi
 */
public enum SearchType {
    NEW("ORDER BY TIME ASC"),
    OLD("ORDER BY TIME DESC"),
    HIGH_SCORE("ORDER BY SCORE ASC"),
    LOW_SCORE("ORDER BY SCORE DESC"),
    FEW_COMMENTS("ORDER BY ARRAY_LENGTH(COMMENTS) ASC"),
    MANY_COMMENTS("ORDER BY ARRAY_LENGTH(COMMENTS) DESC");
    private String querySyntax;

    SearchType(String querySyntax){
        this.querySyntax = querySyntax;
    }

    String getQuerySyntax(){
        return querySyntax;
    }
}