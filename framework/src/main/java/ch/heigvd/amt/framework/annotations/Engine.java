package ch.heigvd.amt.framework.annotations;

import ch.heigvd.amt.application.MyService;
import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.config.Configuration;
import ch.heigvd.amt.framework.config.ManagedComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Engine {

  public void start() {
    try {
      scanClassPath();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  Set<Class> scanClassPath() throws Exception {
    ConfigurationBuilder config = new ConfigurationBuilder()
      .setUrls(ClasspathHelper.forClassLoader())
      .setScanners(new TypeElementsScanner(), new SubTypesScanner(false), new FieldAnnotationsScanner());
    Reflections reflections = new Reflections(config);
    Set<String> types = reflections.getAllTypes();
    Set services = reflections.<IService>getSubTypesOf(IService.class);
    Set<Field> injections = reflections.getFieldsAnnotatedWith(AmtInject.class);

    Field injection = injections.iterator().next();
    injection.setAccessible(true);

    Constructor clientConstructor = injection.getDeclaringClass().getDeclaredConstructor();
    clientConstructor.setAccessible(true);

    Constructor serviceConstructor = injection.getType().getDeclaredConstructor();
    serviceConstructor.setAccessible(true);

    final Object service = serviceConstructor.newInstance();
    final Object client = clientConstructor.newInstance();

    injection.set(client, service);

    Class<?> dynamicType = new ByteBuddy()
      .subclass(injection.getType())
      .method(ElementMatchers.<MethodDescription>any())
      .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          System.out.println("I can do my stuff, audit and co");
          return method.invoke(service);
        }
      }))
      .make()
      .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
      .getLoaded();

    MyService proxy = (MyService) dynamicType.newInstance();
    System.out.println(proxy.getName());


    return services;
  }

  public void parse() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      Configuration config = mapper.readValue(new File("src/main/resources/managed-components.yml"), Configuration.class);
      config.getManagedComponents();
      Collection<ManagedComponent> components = config.getManagedComponents().values();
      Map<String, Object> servicesRegistry = new HashMap<String, Object>();
      for (ManagedComponent component : components) {
        servicesRegistry.put(component.getInterfaceFqdn(), Class.forName(component.getImplementationFqdn()).newInstance());
      }
      System.out.println(servicesRegistry);

    } catch (Exception e) {
      e.printStackTrace();
    }


  }
  public static void main(String[] args) {
    new Engine().parse();
  }
  // new Engine().start();
}


