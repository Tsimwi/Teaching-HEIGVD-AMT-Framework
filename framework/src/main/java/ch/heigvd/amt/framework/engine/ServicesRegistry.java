package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.ServiceLookupException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ServicesRegistry {

  Map<String, IService> registry = new HashMap();

  public void register(String name, String interfaceFullyQualifiedName, String implementationFullyQualifiedName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    IService service = (IService) Class.forName(implementationFullyQualifiedName).newInstance();
    registry.put(name, service);
  }

  public void register(String name, IService service) {
    Class[] interfaces = { IService.class };
    Method[] methods = IService.class.getMethods();
    IService proxiedService;
    proxiedService = (IService) Proxy.newProxyInstance(service.getClass().getClassLoader(), interfaces, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Intercepted call to " + method.getName());
        // System.out.println("Intercepted call to " + method.getName() + " on " + service + " via " + proxy);
        return method.invoke(service, args);
      }
    });
    registry.put(name, proxiedService);
  }

  public IService lookup(String name) throws ServiceLookupException {
    IService service = registry.get(name);
    if (service == null) {
      throw new ServiceLookupException("Could not find service named " + name);
    }
    return service;
  }
}
