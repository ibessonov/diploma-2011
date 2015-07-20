package sl.elements.real;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.unary.UnaryOperationGetter;

enum RealUnaryOperation implements UnaryOperationGetter<RealElement> {

    MINUS {

        @Override
        public Type type() {
            return RealType.get();
        }

        @Override
        public UnaryOperationExecutor get(RealElement element) {
            return new UnaryOperationExecutor<RealElement>(element) {

                @Override
                public StackElement apply() throws StackElementException {
                    return new RealElement(-src.value());
                }
            };
        }
    };

    @Override
    public abstract Type type();

    @Override
    public abstract UnaryOperationExecutor get(RealElement element);
}
