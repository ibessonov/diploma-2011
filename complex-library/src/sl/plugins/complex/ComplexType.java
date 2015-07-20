package sl.plugins.complex;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.elements.real.RealElement;
import sl.elements.real.RealType;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public class ComplexType extends DefaultType {

    private static final Type instance = new ComplexType();

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new ComplexElement(0, 0);
    }

    @Override
    public Type convert(Type type) throws StackElementException {
        try {
            return super.convert(type);
        } catch(StackElementException ex) {
            try {
                RealType.get().convert(type);
                return ComplexType.get();
            } catch (StackElementException ex2) {
                throw ex;
            }
        }
    }

    @Override
    public StackElement convert(StackElement instance) throws StackElementException {
        try {
            return super.convert(instance);
        } catch(StackElementException ex) {
            try {
                RealElement real = (RealElement) RealType.get().convert(instance);
                return new ComplexElement(real.value(), 0);
            } catch (StackElementException ex2) {
                throw ex;
            }
        }
    }

    @Override
    public Type operationResult(UnaryOperation id) throws StackElementException {
        try {
            return ComplexUnaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return ComplexBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "компл";
    }
}
