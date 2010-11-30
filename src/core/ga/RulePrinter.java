package core.ga;

import core.io.dataframe.Mapper;
import java.util.List;

/**
 * @author Rekin
 */
public class RulePrinter {

    Mapper map;

    public RulePrinter(Mapper map) {
        this.map = map;
    }

    public String print(Rule r) {
        if (r.getRelevantSelectors().isEmpty()) {
            return "Class=" + r.getClazz();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");
        final List<Selector> sels = r.getAllSelectors();
        for (int i = 0; i < sels.size(); i++) {
            Selector sel = sels.get(i);
            if (sel.on) {
                if (i != 0) {
                    sb.append(" AND ");
                }
                sb.append("Attr").append(i).append(sel.op).append(sel.val);
            }
        }
        sb.append(" THEN Class=").append(r.getClazz());
        return sb.toString();
    }

    public String prettyPrint(Rule r) {
        if (r.getRelevantSelectors().isEmpty()) {
            return "Class=" + map.getClassmap().get(r.getClazz());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");
        final List<Selector> sels = r.getAllSelectors();
        boolean first = true;
        for (int i = 0; i < sels.size(); i++) {
            Selector sel = sels.get(i);
            if(!sel.on) continue;
            if (!first) {
                sb.append(" AND ");
            }
            sb.append(map.nameOf(i)).append(sel.op).append(map.valueMap(i).get(sel.val));
            first = false;
        }
        sb.append(" THEN ").append(map.getClazzName()).append("=");
        sb.append(map.getClassmap().get(r.getClazz()));
        return sb.toString();
    }
    public String fullPrettyPrint(Rule r) {
        if (r.getRelevantSelectors().isEmpty()) {
            return "Class=" + map.getClassmap().get(r.getClazz());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");
        final List<Selector> sels = r.getAllSelectors();
        for (int i = 0; i < sels.size(); i++) {
            Selector sel = sels.get(i);
            if (i != 0) {
                sb.append("\tAND ");
            }
            sb.append(map.nameOf(i)).append(sel.op).append(map.valueMap(i).get(sel.val));
            if(!sel.on) sb.append("*");
        }
        sb.append(" THEN ").append(map.getClazzName()).append("=");
        sb.append(map.getClassmap().get(r.getClazz()));
        return sb.toString();
    }
}
