package sl.plugins;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import sl.elements.DefaultType;
import sl.elements.Type;
import sl.parser.ParseException;
import sl.parser.ParseTree;
import sl.parser.ParseTreeBuilder;
import sl.parser.Parser;
import sl.parser.SimpleParseTreeBuilder;
import sl.parser.nodes.ParseTreeVisitorException;
import sl.program.Program;
import sl.translator.SLTranslator;

/**
 * Класс для вспомогательных методов
 * @author Полевая Евгения
 */
public final class SLTranslatorUtils {

    private SLTranslatorUtils() {
    }

    /**
     * Загрузка из jar файла всех функций и типов. Функциями считаются классы,
     * расширяющие {@link sl.functions.ExternalFunction}. Типами считаются классы,
     * расширяющие {@link sl.elements.Type}. Созданные экземпляры всех этих
     * классов добавляются в фабрики {@link sl.functions.FunctionsFactory} и
     * {@link sl.functions.TypesFactory} соответственно
     * @param path Путь к загружаемому jar файлу
     * @throws Exception Возникает в одном из трех случаев:
     * <ul>
     *   <li>не удалось загрузить jar файл</li>
     *   <li>не удалось загрузить класс из jar файла</li>
     *   <li>не удалось создать экземпляр загруженного класса</li>
     * </ul>
     */
    public static void loadPlugin(String path) throws Exception {
        ClassLoader loader = new URLClassLoader(new URL[]{
                    new URL("file", "localhost", path)
                });
        JarFile jarFile = new JarFile(path);
        FunctionsFactory functionsFactory = FunctionsFactory.instance();
        TypesFactory typesFactory = TypesFactory.instance();
        for (Enumeration<JarEntry> iterator = jarFile.entries();
                iterator.hasMoreElements();) {
            String entryName = iterator.nextElement().toString();
            if (entryName.endsWith(".class") && !entryName.contains("$")) {
                String className = entryName.replace('/', '.');
                className = className.substring(0, className.length() - 6);
                Class clazz = loader.loadClass(className);
                Class zuper = clazz.getSuperclass();
                if (ExternalFunction.class.equals(zuper)) {
                    Function function = (Function) clazz.newInstance();
                    functionsFactory.add(function);
                } else if (DefaultType.class.equals(zuper)) {
                    Type type = (Type) clazz.newInstance();
                    typesFactory.add(type);
                }
            }
        }
    }

    /**
     * Получить по тексту программы соответствующий объект Program
     * @param text текст программы
     * @return объект Program
     * @throws ParseException синтаксическая ошибка
     * @throws ParseTreeVisitorException ошибка обхода дерева разбора
     */
    public static Program stringToProgram(String text)
            throws ParseException, ParseTreeVisitorException {
        return stringToSLTranslator(text).getProgram();
    }

    public static SLTranslator stringToSLTranslator(String text)
            throws ParseException, ParseTreeVisitorException {
        InputStream input = new ByteArrayInputStream(text.getBytes());
        Parser parser = new Parser(input);
        ParseTreeBuilder builder = new SimpleParseTreeBuilder();
        parser.parse(builder);
        ParseTree tree = builder.result();
        SLTranslator translator = new SLTranslator();
        tree.traverse(translator);
        return translator;
    }
}
