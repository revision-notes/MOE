/*
 * Copyright (c) 2011 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.moe.client;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Interface for MOE to interact with the local filesystem.
 */
public interface FileSystem {

  /**
   * Finds a Temporary Directory starting with prefix that lasts as long as the current task. This
   * is equivalent to calling {@link #getTemporaryDirectory(String, Lifetime)} with
   * {@link Lifetimes#currentTask()}.
   *
   * @param prefix  a prefix for the basename of the created directory
   * @return a path to an uncreated, available temporary directory
   */
  // TODO(user): Delete all usages of this method in favor of the explicit form.
  public File getTemporaryDirectory(String prefix);

  /**
   * Finds a Temporary Directory starting with prefix, with the given lifetime.
   *
   * @param prefix  a prefix for the basename of the created directory
   * @param lifetime  a {@code Lifetime} specifying the clean-up behavior of this temp dir
   * @return a path to an uncreated, available temporary directory
   */
  public File getTemporaryDirectory(String prefix, Lifetime lifetime);

  /**
   * Deletes files/directories created by {@link #getTemporaryDirectory(String, Lifetime)} whose
   * {@code Lifetime}s specify deletion at this juncture.
   *
   * @see #setLifetime(File, Lifetime)
   */
  public void cleanUpTempDirs() throws IOException;

  /**
   * Sets the {@link Lifetime} for a path. The path must have been provided by
   * {@link #getTemporaryDirectory(String, Lifetime)}.
   */
  public void setLifetime(File path, Lifetime lifetime);

  /**
   * Find the relative names of files under  path.
   *
   * NB: returns only files, not directories
   */
  public Set<File> findFiles(File path);

  /**
   * Returns an array of files and directories under path.
   */
  // TODO(user): Return List instead of array.
  public File[] listFiles(File path);

  /**
   * Returns whether the file exists.
   */
  public boolean exists(File f);

  /**
   * Returns the file's name.
   */
  public String getName(File f);

  /**
   * Returns whether the file is a file.
   */
  public boolean isFile(File f);

  /**
   * Returns whether the file is a directory.
   */
  public boolean isDirectory(File f);

  /**
   * Returns whether the file is executable.
   */
  public boolean isExecutable(File f);

  /**
   * Returns whether the file is readable
   */
  public boolean isReadable(File f);

  /**
   * Makes a file executable for all users.
   */
  public void setExecutable(File f);

  /**
   * Makes a file non-executable for all users.
   */
  public void setNonExecutable(File f);

  /**
   * Make the parent directory for f exist.
   */
  public void makeDirsForFile(File f) throws IOException;

  /**
   * Make the directory f exist.
   */
  public void makeDirs(File f) throws IOException;

  /**
   * Copy File src's contents into dest.
   */
  public void copyFile(File src, File dest) throws IOException;

  /**
   * Copy the contents of directory {@code src} into the location represented by the directory
   * {@code dest}. Note, this will not behave as the unix {@code mv} command in that if {@code dest}
   * exists, it will simply poplate it, not populate a sub-folder within {@code dest} with {@code
   * src}'s basename.
   */
  public void copyDirectory(File src, File dest) throws IOException;

  /** Write contents to File f. */
  public void write(String contents, File f) throws IOException;

  /**
   * Deletes a file or directory and all contents recursively.
   */
  public void deleteRecursively(File file) throws IOException;

  /**
   * Extracts a resource into a file.
   *
   * @param resource  the name of the resource to extract
   *
   * @return a path to the resource in the file system
   */
  public File getResourceAsFile(String resource) throws IOException;

  /**
   * Reads all characters from f into a String
   */
  public String fileToString(File f) throws IOException;

  /**
   * A specification of whether a temporary directory should be cleaned up on a call to
   * {@link FileSystem#cleanUpTempDirs()}. On clean-up, each temporary directory's {@code Lifetime}
   * is looked up, and {@link #shouldCleanUp()} is called.
   */
  public static interface Lifetime {

    /**
     * Returns whether a temporary directory with this {@code Lifetime} should be cleaned up now.
     *
     * @see FileSystem#cleanUpTempDirs()
     */
    boolean shouldCleanUp();
  }
}
