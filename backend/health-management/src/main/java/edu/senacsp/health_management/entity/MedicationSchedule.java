package edu.senacsp.health_management.entity;

import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.MedicationStripe;
import jakarta.persistence.*;

/**
 * Medication data that complements a {@link Schedule} of type {@code MEDICATION}.
 *
 * <p>Shares its primary key with the schedule ({@code @MapsId}): the {@code schedule_id}
 * column is at the same time the PK and the FK, so each schedule has at most one row here.
 * The service must only create it for schedules whose type is {@code MEDICATION}.
 *
 * <p>Has no timestamps or {@code active} flag of its own: those live in the parent schedule.
 */
@Entity
public class MedicationSchedule {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String brand;

    @Column(nullable = false)
    private String medicineName;

    private String activeIngredient;

    @Column(nullable = false)
    private Double dosageAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DosageUnit dosageUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationStripe stripe = MedicationStripe.NONE;

    private String photoUrl;

    protected MedicationSchedule() {} // Required by Hibernate

    public MedicationSchedule(Schedule schedule, String medicineName,
                              Double dosageAmount, DosageUnit dosageUnit) {
        this.schedule = schedule;
        this.medicineName = medicineName;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
    }

    //-------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public Double getDosageAmount() {
        return dosageAmount;
    }

    public void setDosageAmount(Double dosageAmount) {
        this.dosageAmount = dosageAmount;
    }

    public DosageUnit getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(DosageUnit dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public MedicationStripe getStripe() {
        return stripe;
    }

    public void setStripe(MedicationStripe stripe) {
        this.stripe = stripe;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}