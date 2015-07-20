package sl.plugins.complex;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.unary.UnaryOperationGetter;

public enum ComplexUnaryOperation implements UnaryOperationGetter<ComplexElement> {

    MINUS {

        @Override
        public Type type() {
            return ComplexType.get();
        }

        @Override
        public UnaryOperationExecutor get(ComplexElement element) {
            return new UnaryOperationExecutor<ComplexElement>(element) {

                @Override
                public StackElement apply() throws StackElementException {
                    return new ComplexElement(-src.real(), -src.image());
                }
            };
        }
    };

    @Override
    public abstract Type type();

    @Override
    public abstract UnaryOperationExecutor get(ComplexElement element);
}
