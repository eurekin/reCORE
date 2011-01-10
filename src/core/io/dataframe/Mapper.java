package core.io.dataframe;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;
import core.io.Interpreter;
import core.io.ResultConsumer;
import core.io.InputStreamProcessor;
import core.io.SplittingLineProcessor;
import java.util.HashSet;
import java.util.Set;

/**
 * Atrybut klasy musi być traktowany specjalnie (podobnie jak w Rule).
 * @author Rekin
 */
public class Mapper {

    // regular attributes
    Map<Integer, Map<Integer, String>> valmap;
    Map<Integer, String> namemap;
    // special case for class
    String clazzName;
    Map<Integer, String> classmap;

    public Mapper(Map<Integer, Map<Integer, String>> valmap,
            Map<Integer, String> namemap) {
        this.valmap = valmap;
        this.namemap = namemap;
        getRidOfClassAttributeFromListOfRegularAttributes();
    }

    public Mapper(Map<Integer, Map<Integer, String>> valmap,
            Map<Integer, String> namemap,
            String clazzName,
            Map<Integer, String> classmap) {
        this.valmap = valmap;
        this.namemap = namemap;
        this.clazzName = clazzName;
        this.classmap = classmap;
    }

    public String nameOf(int attr) {
        return namemap.get(attr);
    }

    public Map<Integer, String> valueMap(int attr) {
        return valmap.get(attr);
    }

    public Map<Integer, String> getClassmap() {
        return classmap;
    }

    public String getClazzName() {
        return clazzName;
    }

    public Mapper(InputStream in) {
        String delimeter = " ";
        valmap = new HashMap<Integer, Map<Integer, String>>();
        namemap = new HashMap<Integer, String>();
        Interpreter interpreter = new Interpreter() {

            public List interpret(String[] args) {
                String attrName = val(args[0]);
                Integer key = key(args[0]);
                namemap.put(key, attrName);

                HashMap<Integer, String> tm = new HashMap<Integer, String>();
                List<String> vals = Arrays.asList(args).subList(1, args.length);
                for (String v : vals) {
                    tm.put(key(v), val(v));
                }
                valmap.put(key, tm);
                return new ArrayList();
            }

            public int key(String s) {
                return Integer.valueOf(s.split(":")[0]);
            }

            public String val(String s) {
                return s.split(":")[1];
            }
        };

        ResultConsumer nullCons = new ResultConsumer() {

            public void consume(List interpreted) {
            }
        };
        SplittingLineProcessor processor =
                new SplittingLineProcessor(delimeter, interpreter, nullCons);
        InputStreamProcessor isp = new InputStreamProcessor(in, processor);
        isp.process();
        getRidOfClassAttributeFromListOfRegularAttributes();
    }

    private void getRidOfClassAttributeFromListOfRegularAttributes() {
        Integer key = -1;
        for (Integer ck : valmap.keySet()) {
            String attrName = namemap.get(ck);
            boolean clazz = attrName.trim().toLowerCase().contains("class");
            if (clazz) {
                key = ck;
            }
        }
        clazzName = namemap.remove(key);
        classmap = valmap.remove(key);
        balanceOutKeysInAbsenceOf(key);
    }

    private void balanceOutKeysInAbsenceOf(Integer key) {
        Set<Integer> keys = new HashSet<Integer>(valmap.keySet());
        for (Integer ck : keys) {
            if (ck < key) {
                continue;
            }
            valmap.put(ck - 1, valmap.remove(ck));
            namemap.put(ck - 1, namemap.remove(ck));
        }
    }
}
