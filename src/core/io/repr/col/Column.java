package core.io.repr.col;

/**
 * Typy kolumn:
 *  Specjalne - linia w pliku, nazwa pliku
 *  Wczytane z pliku:
 *    String (pamiętana pula),
 *    Int (zakres: min, max, inne statystyki, m.in. średnia, mediana),
 *    Double - podział wg. typów
 *  Wyniki klasyfikacji - lepiej osobna tabela z kluczem
 * 
 * Co do min/max oraz dostępnych wartości to będą one wymagane przy
 * konstruowaniu algorytmu genetycznego. Reszta jest bardziej pomocnicza.
 * Przykładowo, bardziej rozbudowane statystyki mogą być pokazy
 * 
 * @author Rekin
 */
public interface Column<T> {

    public T get(int i);
    public void add(T el);
}