package sl.elements.bool;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.unary.UnaryOperationGetter;

enum BooleanUnaryOperation implements UnaryOperationGetter<BooleanElement> {

    NEGATION {

        @Override
        public UnaryOperationExecutor get(BooleanElement element) {
            return new UnaryOperationExecutor<BooleanElement>(element) {

                @Override
                public StackElement apply() throws StackElementException {
                    return BooleanElement.get(!src.value());
                }
            };
        }
    };

    @Override
    public Type type() {
        return BooleanType.get();
    }

    @Override
    public abstract UnaryOperationExecutor get(BooleanElement element);
}
