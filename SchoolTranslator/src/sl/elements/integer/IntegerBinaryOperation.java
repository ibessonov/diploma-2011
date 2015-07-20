package sl.elements.integer;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.elements.real.RealElement;
import sl.elements.real.RealType;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

enum IntegerBinaryOperation implements BinaryOperationGetter<IntegerElement> {

    ADDITION {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return new IntegerElement(left.value() + right.value());
        }

        @Override
        public Type type() {
            return IntegerType.get();
        }
    },
    SUBTRACTION {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return new IntegerElement(left.value() - right.value());
        }

        @Override
        public Type type() {
            return IntegerType.get();
        }
    },
    MULTIPLICATION {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return new IntegerElement(left.value() * right.value());
        }

        @Override
        public Type type() {
            return IntegerType.get();
        }
    },
    DIVISION {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            if (right.value() == 0) {
                throw new RuntimeException("Деление на ноль");
            }
            return new RealElement((double) left.value() / right.value());
        }

        @Override
        public Type type() {
            return RealType.get();
        }
    },
    COMPARISON_LESS {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() < right.value());
        }
    },
    COMPARISON_NOT_LESS {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() >= right.value());
        }
    },
    COMPARISON_GREATER {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() > right.value());
        }
    },
    COMPARISON_NOT_GREATER {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() <= right.value());
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() == right.value());
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(IntegerElement left, IntegerElement right) {
            return BooleanElement.get(left.value() != right.value());
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    protected abstract StackElement execute(IntegerElement left, IntegerElement right);

    @Override
    public BinaryOperationExecutor get(IntegerElement elem) {
        return new BinaryOperationExecutor<IntegerElement>(elem) {

            @Override
            protected StackElement apply(IntegerElement left, IntegerElement right) {
                return execute(left, right);
            }
        };
    }
}
