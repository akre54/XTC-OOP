/* info about file dependencies for DependencyFinder,
 * Translator, and InheritanceBuilder
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

public class FileDependency {

    public String fullPath;
    public DependencyOrigin origin;

    public FileDependency(String fullPath, DependencyOrigin origin) {
        this.fullPath = fullPath;
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileDependency) {
            FileDependency other = (FileDependency)o;
            return this.fullPath.equals((other.fullPath));
        }
        return false;
    }


    @Override
    public int hashCode() {
        return fullPath.hashCode();
    }

    @Override
    public String toString() {
        return fullPath + ", " + origin;
    }
}