package group1.project.data.queries;

import group1.project.data.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Abstract Query class whose implementations are used to interface between the frontend and backend
 * @author Kevin Troisi
 */
abstract public class Query {

    protected ResultSet result;
    protected int CHUNK_SIZE;

    public abstract List<? extends Data> nextChunk();
    public abstract List<? extends Data> previousChunk();

    public List<? extends Data> nextChunk(Function<ResultSet, Data> function) {
        List<Data> list = new ArrayList<>(CHUNK_SIZE);

        try {
            for (int i = 0; result.next() && i < CHUNK_SIZE; i++)
                list.add(function.apply(result));
        } catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<? extends Data> previousChunk(Function<ResultSet, Data> function) {
        try {
            result.absolute(result.getRow() - 2 * (CHUNK_SIZE + 1));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return nextChunk(function);
    }
}
