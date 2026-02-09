package br.com.hospidata.stock_service.common;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.util.List;

public class RsqlSpecification<T> implements Specification<T> {

    private final ComparisonNode node;

    public RsqlSpecification(ComparisonNode node) {
        this.node = node;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb
    ) {
        String field = node.getSelector();
        String operator = node.getOperator().getSymbol();
        List<String> args = node.getArguments();

        Path<?> path = root.get(field);

        return switch (operator) {
            case "==" -> cb.equal(path, cast(path, args.get(0)));
            case "!=" -> cb.notEqual(path, cast(path, args.get(0)));
            case "=in=" -> path.in(castList(path, args));
            case "=out=" -> cb.not(path.in(castList(path, args)));
            case ">" -> cb.greaterThan(path.as(String.class), args.get(0));
            case "<" -> cb.lessThan(path.as(String.class), args.get(0));
            case ">=" -> cb.greaterThanOrEqualTo(path.as(String.class), args.get(0));
            case "<=" -> cb.lessThanOrEqualTo(path.as(String.class), args.get(0));
            default -> throw new IllegalArgumentException("Operador não suportado: " + operator);
        };
    }

    private Object cast(Path<?> path, String value) {
        Class<?> type = path.getJavaType();

        if (type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }
        if (Enum.class.isAssignableFrom(type)) {
            return Enum.valueOf((Class<Enum>) type, value);
        }
        return value;
    }

    private List<?> castList(Path<?> path, List<String> values) {
        return values.stream()
                .map(v -> cast(path, v))
                .toList();
    }
}
