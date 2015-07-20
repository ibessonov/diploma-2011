package sl.plugins;

import java.util.HashMap;
import java.util.Map;
import sl.elements.Type;
import sl.elements.bool.BooleanType;
import sl.elements.character.CharacterType;
import sl.elements.integer.IntegerType;
import sl.elements.real.RealType;
import sl.elements.string.StringType;
import sl.parser.TokenKind;

/**
 * Класс представляет собой фабрику всех доступных типов языка. Реализован как
 * Singleton, чтобы набор типов был общим для всех.
 * @author Полевая Евгения
 */
public final class TypesFactory {

    static private TypesFactory instance = null;
    private Map<String, Type> map;

    private TypesFactory() {
        map = new HashMap<String, Type>();
        map.put(TokenKind.INTEGER.toString(), IntegerType.get());
        map.put(TokenKind.REAL.toString(), RealType.get());
        map.put(TokenKind.CHAR.toString(), CharacterType.get());
        map.put(TokenKind.BOOLEAN.toString(), BooleanType.get());
        map.put(TokenKind.STRING.toString(), StringType.get());
    }

    /**
     * Добавление типа к фабрике. Если одноименный типуже существует,
     * то он заменяется на новый
     * @param type новый тип. Его имя получается вызовом метода toString()
     */
    public synchronized void add(Type type) {
        map.put(type.toString(), type);
    }

    /**
     * Получение типа по имени
     * @param name имя типа
     * @return объект {@link Type}, представляющий тип.
     * null, если тип с таким именем не найден
     */
    public synchronized Type get(String name) {
        return map.get(name);
    }

    /**
     * Получение единственного экземпляра класса
     * @return объект TypesFactory
     */
    public static TypesFactory instance() {
        return (instance == null)
                ? (instance = new TypesFactory())
                : (instance);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
