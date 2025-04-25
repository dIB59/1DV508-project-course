package org.example.features.admin;

import org.example.shared.SceneRouter;

public class AdminController {

  private final SceneRouter sceneRouter;

  /**
   * Instantiates a new Admin controller.
   *
   * @param sceneRouter the scene router
   */
  public AdminController(SceneRouter sceneRouter) {
    this.sceneRouter = sceneRouter;
  }

  /** Go to admin page. */
  public void goToDashboard() {
    sceneRouter.goToDashboardPage();
  }



}
