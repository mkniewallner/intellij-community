package com.intellij.cyclicDependencies.actions;

import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.AnalysisScopeBundle;
import com.intellij.cyclicDependencies.CyclicDependenciesBuilder;
import com.intellij.cyclicDependencies.ui.CyclicDependenciesPanel;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.packageDependencies.DependencyValidationManager;
import com.intellij.packageDependencies.DependencyValidationManagerImpl;
import com.intellij.peer.PeerFactory;
import com.intellij.ui.content.Content;

/**
 * User: anna
 * Date: Jan 31, 2005
 */
public class CyclicDependenciesHandler {
  private Project myProject;
  private AnalysisScope myScope;

  public CyclicDependenciesHandler(Project project, AnalysisScope scope) {
    myProject = project;
    myScope = scope;
  }

  public void analyze() {
    final CyclicDependenciesBuilder builder = new CyclicDependenciesBuilder(myProject, myScope);
    if (ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {
      public void run() {
        builder.analyze();
      }
    }, AnalysisScopeBundle.message("package.dependencies.progress.title"), true, myProject)) {
      CyclicDependenciesPanel panel = new CyclicDependenciesPanel(myProject, builder);
      Content content = PeerFactory.getInstance().getContentFactory().createContent(panel,
                                                                                    AnalysisScopeBundle.message("action.analyzing.cyclic.dependencies.in.scope", builder.getScope().getDisplayName()),
                                                                                    false);
      content.setDisposer(panel);
      panel.setContent(content);
      ((DependencyValidationManagerImpl)DependencyValidationManager.getInstance(myProject)).addContent(content);
    }
  }
}
