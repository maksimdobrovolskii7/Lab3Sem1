package com.jujutsu.archive.dto;

public class ReportParameters {
    private boolean showBasic = true;
    private boolean showCurse = true;
    private boolean showSorcerers = true;
    private boolean showTechniques = true;
    private boolean showExtensions = false;
    public boolean isShowBasic() { return showBasic; }
    public void setShowBasic(boolean showBasic) { this.showBasic = showBasic; }
    public boolean isShowCurse() { return showCurse; }
    public void setShowCurse(boolean showCurse) { this.showCurse = showCurse; }
    public boolean isShowSorcerers() { return showSorcerers; }
    public void setShowSorcerers(boolean showSorcerers) { this.showSorcerers = showSorcerers; }
    public boolean isShowTechniques() { return showTechniques; }
    public void setShowTechniques(boolean showTechniques) { this.showTechniques = showTechniques; }
    public boolean isShowExtensions() { return showExtensions; }
    public void setShowExtensions(boolean showExtensions) { this.showExtensions = showExtensions; }
}