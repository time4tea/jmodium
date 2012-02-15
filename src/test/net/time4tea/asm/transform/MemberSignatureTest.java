package net.time4tea.asm.transform;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MemberSignatureTest {
    
    @Test
    public void constructorWithMethod() throws Exception {
        Method expected = System.class.getMethod("currentTimeMillis");

        MemberSignature signature = new MemberSignature(expected);
        
        assertThat(signature.className(), equalTo("java.lang.System"));
    }
    
    @Test
    public void methodNoArguments() throws Exception {
        Method expected = System.class.getMethod("currentTimeMillis");
        Method method = new MemberSignature(System.class, "currentTimeMillis", "()V").asMethod();

        assertThat(method, equalTo(expected));
    }
    
    @Test
    public void methodWithArguments() throws Exception {
        Method expected = System.class.getMethod("getProperty",String.class, String.class);
        Method method = new MemberSignature(System.class, "getProperty", "(Ljava/lang/String;Ljava/lang/String;)V").asMethod();

        assertThat(method, equalTo(expected));
    }
    
    public interface FirstClass {
        void foo(String a);
    }
    
    public interface SecondClass {
        void foo(String a);
        void foo(String a, String b, int c);
    }
    
    @Test
    public void sameMethodOnDifferentClass() throws Exception {
        MemberSignature signature = new MemberSignature(FirstClass.class.getMethod("foo", String.class));

        Method expected = SecondClass.class.getMethod("foo", String.class);

        Method method = signature.asMethodOnClass(SecondClass.class);

        assertThat(method, equalTo(expected));
    }

    @Test
    public void sameMethodNameDifferentSignature() throws Exception {
        MemberSignature signature = new MemberSignature(FirstClass.class.getMethod("foo", String.class));

        Method expected = SecondClass.class.getMethod("foo", String.class, String.class, int.class);

        Method method = signature.asMethodOnClass(SecondClass.class, String.class, int.class);

        assertThat(method, equalTo(expected));
    }
    
    
}
