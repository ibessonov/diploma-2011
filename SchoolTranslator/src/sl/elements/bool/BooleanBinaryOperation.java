package sl.elements.bool;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

enum BooleanBinaryOperation implements BinaryOperationGetter<BooleanElement> {

    CONJUNCTION {

        @Override
        protected StackElement execute(BooleanElement left, BooleanElement right) {
            return BooleanElement.get(left.value() && right.value());
        }
    },
    DISJUNCTION {

        @Override
        protected StackElement execute(BooleanElement left, BooleanElement right) {
            return BooleanElement.get(left.value() || right.value());
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(BooleanElement left, BooleanElement right) {
            return BooleanElement.get(left.value() == right.value());
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(BooleanElement left, BooleanElement right) {
            return BooleanElement.get(left.value() == right.value());
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    protected abstract StackElement execute(BooleanElement left, BooleanElement right);

    @Override
    public BinaryOperationExecutor get(BooleanElement elem) {
        return new BinaryOperationExecutor<BooleanElement>(elem) {

            @Override
            protected StackElement apply(BooleanElement left, BooleanElement right) {
                return execute(left, right);
            }
        };
    }
}
