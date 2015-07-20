package sl.elements.integer;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.unary.UnaryOperationGetter;

enum IntegerUnaryOperation implements UnaryOperationGetter<IntegerElement> {

    MINUS {

        @Override
        public Type type() {
            return IntegerType.get();
        }

        @Override
        public UnaryOperationExecutor get(IntegerElement element) {
            return new UnaryOperationExecutor<IntegerElement>(element) {

                @Override
                public StackElement apply() throws StackElementException {
                    return new IntegerElement(-src.value());
                }
            };
        }
    };

    @Override
    public abstract Type type();

    @Override
    public abstract UnaryOperationExecutor get(IntegerElement element);
}
