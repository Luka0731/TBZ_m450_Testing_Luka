package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;

import static ch.tbz.m450.util.SortField.*;



public class AddressComparator implements Comparator<Address> {
    private final SortField sortField;

    public AddressComparator() {
        this(LASTNAME);
    }

    public AddressComparator(SortField sortField) {
        this.sortField = sortField;
    }



    @Override
    public int compare(Address a1, Address a2) {
        int result = compareBy(sortField, a1, a2);
        if (result == 0 && sortField == LASTNAME) {
            result = compareBy(FIRSTNAME, a1, a2);
        }
        return result;
    }



    // |----- helper methods -----|

    private int compareBy(SortField field, Address a1, Address a2) {
        return switch (field) {
            case ID -> Integer.compare(a1.getId(), a2.getId());
            case FIRSTNAME -> compareNullable(a1.getFirstname(), a2.getFirstname());
            case LASTNAME -> compareNullable(a1.getLastname(), a2.getLastname());
            case PHONENUMBER -> compareNullable(a1.getPhonenumber(), a2.getPhonenumber());
            case REGISTRATION_DATE -> compareNullable(a1.getRegistrationDate(), a2.getRegistrationDate());
        };
    }

    private <T extends Comparable<T>> int compareNullable(T v1, T v2) {
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return 1;
        }
        if (v2 == null) {
            return -1;
        }
        if (v1 instanceof String s1 && v2 instanceof String s2) {
            return s1.compareToIgnoreCase(s2);
        }
        return v1.compareTo(v2);
    }
}