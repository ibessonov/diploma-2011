package sl.elements.string;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

enum StringBinaryOperation implements BinaryOperationGetter<StringElement> {

    ADDITION {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return new StringElement(left.value() + right.value());
        }

        @Override
        public Type type() {
            return StringType.get();
        }
    },
    COMPARISON_LESS {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(left.value().compareTo(right.value()) < 0);
        }
    },
    COMPARISON_NOT_LESS {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(left.value().compareTo(right.value()) >= 0);
        }
    },
    COMPARISON_GREATER {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(left.value().compareTo(right.value()) > 0);
        }
    },
    COMPARISON_NOT_GREATER {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(left.value().compareTo(right.value()) <= 0);
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(left.value().equals(right.value()));
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(StringElement left, StringElement right) {
            return BooleanElement.get(!left.value().equals(right.value()));
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    protected abstract StackElement execute(StringElement left, StringElement right);

    @Override
    public BinaryOperationExecutor get(StringElement elem) {
        return new BinaryOperationExecutor<StringElement>(elem) {

            @Override
            protected StackElement apply(StringElement left, StringElement right) {
                return execute(left, right);
            }
        };
    }
}
