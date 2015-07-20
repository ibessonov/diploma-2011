package sl.elements.character;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.binary.BinaryOperationGetter;

enum CharacterBinaryOperation implements BinaryOperationGetter<CharacterElement> {

    COMPARISON_LESS {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() < right.value());
        }
    },
    COMPARISON_NOT_LESS {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() >= right.value());
        }
    },
    COMPARISON_GREATER {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() > right.value());
        }
    },
    COMPARISON_NOT_GREATER {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() <= right.value());
        }
    },
    COMPARISON_EQUALS {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() == right.value());
        }
    },
    COMPARISON_NOT_EQUALS {

        @Override
        protected StackElement execute(CharacterElement left, CharacterElement right) {
            return BooleanElement.get(left.value() != right.value());
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    protected abstract StackElement execute(CharacterElement left, CharacterElement right);

    @Override
    public BinaryOperationExecutor get(CharacterElement elem) {
        return new BinaryOperationExecutor<CharacterElement>(elem) {

            @Override
            protected StackElement apply(CharacterElement left, CharacterElement right) {
                return execute(left, right);
            }
        };
    }
}
