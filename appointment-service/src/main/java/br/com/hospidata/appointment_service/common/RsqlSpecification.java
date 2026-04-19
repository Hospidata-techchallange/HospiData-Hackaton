package br.com.hospidata.appointment_service.common;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"rawtypes", "unchecked"})
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

        Path<?> path = resolvePath(root, field);

        return switch (operator) {
            case "==" -> cb.equal(path, cast(path, args.get(0)));
            case "!=" -> cb.notEqual(path, cast(path, args.get(0)));
            case "=in=" -> path.in(castList(path, args));
            case "=out=" -> cb.not(path.in(castList(path, args)));
            case ">" -> cb.greaterThan((Path<Comparable>) path, (Comparable) cast(path, args.get(0)));
            case "<" -> cb.lessThan((Path<Comparable>) path, (Comparable) cast(path, args.get(0)));
            case ">=" -> cb.greaterThanOrEqualTo((Path<Comparable>) path, (Comparable) cast(path, args.get(0)));
            case "<=" -> cb.lessThanOrEqualTo((Path<Comparable>) path, (Comparable) cast(path, args.get(0)));
            default -> throw new IllegalArgumentException("Operador não suportado: " + operator);
        };
    }

    private Path<?> resolvePath(Root<T> root, String field) {
        String[] parts = field.split("\\.");

        Path<?> path = root.get(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }

        return path;
    }

    private Object cast(Path<?> path, String value) {
        Class<?> type = path.getJavaType();

        if (type.equals(String.class)) {
            return value;
        }

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.valueOf(value);
        }

        if (type.equals(UUID.class)) {
            return UUID.fromString(value);
        }

        if (type.equals(LocalDate.class)) {
            return LocalDate.parse(value);
        }

        if (type.equals(LocalTime.class)) {
            return LocalTime.parse(value);
        }

        if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(value);
        }

        if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.valueOf(value);
        }

        if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.valueOf(value);
        }

        if (Enum.class.isAssignableFrom(type)) {
            return Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), value);
        }

        return value;
    }

    private List<?> castList(Path<?> path, List<String> values) {
        return values.stream()
                .map(value -> cast(path, value))
                .toList();
    }
}