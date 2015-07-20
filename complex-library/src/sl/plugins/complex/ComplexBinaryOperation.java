package sl.plugins.complex;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

public enum ComplexBinaryOperation implements BinaryOperationGetter<ComplexElement> {

    ADDITION {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            return new ComplexElement(left.real() + right.real(),
                    left.image() + right.image());
        }

        @Override
        public Type type() {
            return ComplexType.get();
        }
    },
    SUBTRACTION {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            return new ComplexElement(left.real() - right.real(),
                    left.image() - right.image());
        }

        @Override
        public Type type() {
            return ComplexType.get();
        }
    },
    MULTIPLICATION {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            return new ComplexElement(
                    left.real() * right.real() - left.image() * right.image(),
                    left.real() * right.image() + left.image() * right.real());
        }

        @Override
        public Type type() {
            return ComplexType.get();
        }
    },
    DIVISION {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            if (right.real() == 0 && right.image() == 0) {
                throw new RuntimeException("Деление на ноль");
            }
            double norm = right.real() * right.real()
                    + right.image() * right.image();

            return new ComplexElement(
                    (left.real() * right.real()
                    + left.image() * right.image()) / norm,
                    (-left.real() * right.image()
                    + left.image() * right.real()) / norm);
        }

        @Override
        public Type type() {
            return ComplexType.get();
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            return BooleanElement.get(left.real() == right.real()
                    && left.image() == right.image());
        }

        @Override
        public Type type() {
            return BooleanType.get();
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(ComplexElement left, ComplexElement right) {
            return BooleanElement.get(left.real() != right.real()
                    || left.image() != right.image());
        }

        @Override
        public Type type() {
            return BooleanType.get();
        }
    };

    @Override
    public Type type() {
        return ComplexType.get();
    }

    protected abstract StackElement execute(ComplexElement left, ComplexElement right);

    @Override
    public BinaryOperationExecutor get(ComplexElement elem) {
        return new BinaryOperationExecutor<ComplexElement>(elem) {

            @Override
            protected StackElement apply(ComplexElement left, ComplexElement right) {
                return execute(left, right);
            }
        };
    }
}
