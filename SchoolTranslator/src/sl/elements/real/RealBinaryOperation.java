package sl.elements.real;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

enum RealBinaryOperation implements BinaryOperationGetter<RealElement> {

    ADDITION {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return new RealElement(left.value() + right.value());
        }

        @Override
        public Type type() {
            return RealType.get();
        }
    },
    SUBTRACTION {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return new RealElement(left.value() - right.value());
        }

        @Override
        public Type type() {
            return RealType.get();
        }
    },
    MULTIPLICATION {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return new RealElement(left.value() * right.value());
        }

        @Override
        public Type type() {
            return RealType.get();
        }
    },
    DIVISION {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
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
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() < right.value());
        }
    },
    COMPARISON_NOT_LESS {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() >= right.value());
        }
    },
    COMPARISON_GREATER {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() > right.value());
        }
    },
    COMPARISON_NOT_GREATER {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() <= right.value());
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() == right.value());
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(RealElement left, RealElement right) {
            return BooleanElement.get(left.value() != right.value());
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    protected abstract StackElement execute(RealElement left, RealElement right);

    @Override
    public BinaryOperationExecutor get(RealElement elem) {
        return new BinaryOperationExecutor<RealElement>(elem) {

            @Override
            protected StackElement apply(RealElement left, RealElement right) {
                return execute(left, right);
            }
        };
    }
}
