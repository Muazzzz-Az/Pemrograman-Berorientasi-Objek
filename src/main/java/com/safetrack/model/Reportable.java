package com.safetrack.model;

/**
 * ABSTRACTION: Interface yang mendefinisikan kontrak pelaporan.
 * Siapapun yang implement interface ini WAJIB menyediakan ketiga method ini.
 * Detail implementasinya tersembunyi dari luar.
 */
public interface Reportable {

    void submitReport();

    String getReportStatus();

    void cancelReport();
}
