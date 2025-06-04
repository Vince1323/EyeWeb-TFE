package com.jorami.eyeapp.dto.exam;

// Importation de l’annotation @JsonProperty pour personnaliser les noms JSON lors de la sérialisation/désérialisation
import com.fasterxml.jackson.annotation.JsonProperty;

import com.jorami.eyeapp.model.Enum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * ExamDto est un Data Transfer Object utilisé pour transférer les données d’un examen ophtalmologique
 * entre le frontend Angular et le backend Spring. Il centralise toutes les informations techniques
 * relevées durant l’examen.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamDto implements BaseExamDto {

    @JsonProperty("id")
    private Long id; // Identifiant unique de l’examen

    @JsonProperty("version")
    private Long version; // Version de l'entité (optimistic locking)

    @JsonProperty("eyeSide")
    private Enum.EyeSide eyeSide; // Côté de l’œil : OD (droit), OS (gauche), OU (les deux)

    @JsonProperty("examType")
    private String examType; // Type d'examen (ex: Pentacam 25 ou 50)

    @JsonProperty("examDate")
    private String examDate; // Date de l'examen au format String (souvent ISO 8601)

    @JsonProperty("examComment")
    private String examComment; // Commentaire libre

    @JsonProperty("examQuality")
    private String examQuality; // Qualité ou statut de l'examen (ex: Valide, Flou)

    @JsonProperty("calculDate")
    private String calculDate; // Date du calcul (ex: biométrie)

    @JsonProperty("eyeStatus")
    private String eyeStatus; // État général de l’œil examiné

    @JsonProperty("selected")
    private boolean selected; // Examen sélectionné comme référence pour les calculs

    // Données biométriques standardisées issues de différents biomètres
    @JsonProperty("al")
    private Float al; // Axial Length (Longueur axiale)

    @JsonProperty("acd")
    private Float acd; // ACD externe (profondeur chambre antérieure externe)

    @JsonProperty("internalAcd")
    private Float internalAcd; // ACD interne (mesurée entre l’endothélium et le cristallin)

    @JsonProperty("pupilDia")
    private Float pupilDia; // Diamètre pupillaire

    @JsonProperty("pupilMin")
    private Float pupilMin; // Diamètre pupillaire minimum (photopique)

    @JsonProperty("pupilMax")
    private Float pupilMax; // Diamètre pupillaire maximum (scotopique)

    @JsonProperty("wtw")
    private Float wtw; // White-to-white (distance cornée blanche à blanche)

    @JsonProperty("cord")
    private Float cord; // Distance entre axe visuel et pupille (cord μ)

    @JsonProperty("z40")
    private Float z40; // Aberration sphérique

    @JsonProperty("hoa")
    private Float hoa; // Aberrations de haut ordre

    @JsonProperty("kappaAngle")
    private Float kappaAngle; // Angle kappa (écart entre axe visuel et pupillaire)

    // Kératométrie et topographie cornéenne
    @JsonProperty("n")
    private Float n; // Indice de réfraction kératométrique

    @JsonProperty("k1")
    private Float k1; // Kératométrie faible

    @JsonProperty("k1Mm")
    private Float k1Mm; // Rayon de courbure k1 en mm

    @JsonProperty("k1Axis")
    private Float k1Axis; // Axe de k1

    @JsonProperty("k2")
    private Float k2; // Kératométrie forte

    @JsonProperty("k2Mm")
    private Float k2Mm; // Rayon de courbure k2 en mm

    @JsonProperty("k2Axis")
    private Float k2Axis; // Axe de k2

    @JsonProperty("kAstig")
    private Float k_astig; // Astigmatisme kératométrique

    @JsonProperty("kAstigAxis")
    private Float k_astigAxis; // Axe d’astigmatisme

    @JsonProperty("kAvg")
    private Float k_avg; // Moyenne des kératométries

    @JsonProperty("rAvg")
    private Float r_avg; // Moyenne des rayons

    @JsonProperty("k1CorneaBack")
    private Float k1CorneaBack; // K1 sur face postérieure

    @JsonProperty("k2CorneaBack")
    private Float k2CorneaBack;

    @JsonProperty("k1AxisCorneaBack")
    private Float k1AxisCorneaBack;

    @JsonProperty("k2AxisCorneaBack")
    private Float k2AxisCorneaBack;

    @JsonProperty("siaCyl")
    private Float siaCyl; // Astigmatisme induit chirurgicalement

    @JsonProperty("siaAxis")
    private Float siaAxis;

    @JsonProperty("snr")
    private Float snr; // Rapport signal/bruit

    @JsonProperty("lensThickness")
    private Float lensThickness; // Épaisseur du cristallin

    @JsonProperty("cct")
    private Float cct; // Épaisseur centrale de la cornée

    @JsonProperty("cctMin")
    private Float cctMin; // Épaisseur minimale mesurée

    @JsonProperty("asphQf")
    private Float asphQf; // Asphéricité de la face avant

    @JsonProperty("asphQb")
    private Float asphQb; // Asphéricité de la face arrière

    @JsonProperty("alStatus")
    private Float alStatus; // Statut du relevé de l’AL

    @JsonProperty("alError")
    private Float alError; // Erreur potentielle de mesure d’AL

    @JsonProperty("kPreRefrAvg")
    private Float k_preRefrAvg; // Moyenne kératométrique avant chirurgie

    @JsonProperty("rPreRefrAvg")
    private Float r_preRefrAvg; // Moyenne des rayons avant chirurgie

    @JsonProperty("manifestRefrDate")
    private String manifestRefrDate;

    @JsonProperty("manifestRefrSph")
    private Float manifestRefrSph; // Sphère

    @JsonProperty("manifestRefrCyl")
    private Float manifestRefrCyl; // Cylindre

    @JsonProperty("manifestRefrAxis")
    private Float manifestRefrAxis; // Axe

    @JsonProperty("manifestRefrVd")
    private Float manifestRefrVd; // Distance vertex

    @JsonProperty("targetRefrSph")
    private Float targetRefrSph; // Sphère visée postop

    @JsonProperty("targetRefrCyl")
    private Float targetRefrCyl; // Cylindre visé postop

    @JsonProperty("machine")
    private String machine; // Nom du biomètre utilisé

    @JsonProperty("importType")
    private String importType; // Type d’importation (manuel, fichier, OCR...)

    private Long patientId; // ID du patient lié à cet examen

}
